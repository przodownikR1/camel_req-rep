package pl.java.scalatech;

import static com.jayway.awaitility.Awaitility.await;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.equalTo;

import java.util.concurrent.Callable;

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
       
         await().atMost(2, SECONDS).until(untilCheckEndpoint(consumer, "seda:out"), equalTo(TRUE));

    }

    private Callable<Boolean> untilCheckEndpoint(ConsumerTemplate ct, String endpoint) {
        return () -> {
            Exchange ex = ct.receive(endpoint);
            log.info("retrieve message ---->  {}",ex.getIn().getBody());
            if (ex != null) {
                return TRUE;
            }
            return FALSE;
        };
    }

   
}
