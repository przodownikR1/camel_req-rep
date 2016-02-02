package pl.java.scalatech;

import static org.hamcrest.Matchers.equalTo;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.awaitility.Awaitility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ContextConfiguration({ "classpath:jmsConfigTest.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class AsyncTest{

    @Autowired
    private CamelContext context;
    @Autowired
    private ConsumerTemplate consumer;
    @Autowired
    private ProducerTemplate producer; 
   
    @Test
    public void testActiveMQ() throws Exception {
          Assertions.assertThat(context).isNotNull();
          Assertions.assertThat(consumer).isNotNull();
          Assertions.assertThat(producer).isNotNull();
          producer.sendBody("seda:in", "sdd");
       
       
         Awaitility.await().atMost(2, TimeUnit.SECONDS).until(untilCheckEndpoint(consumer, "seda:out"), equalTo(true));

    }

    private Callable<Boolean> untilCheckEndpoint(ConsumerTemplate ct, String endpoint) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Exchange ex = ct.receive(endpoint);
                log.info("retrieve message ---->  {}",ex);
                if (ex != null) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
        };
    }

   
}
