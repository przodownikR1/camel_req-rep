package pl.java.scalatech.rr;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@ContextConfiguration({ "classpath:springCamelTest.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringRRTest {

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
        context.start();
        producer.sendBody("seda:in", "sdd");
     
        Thread.sleep(2000l);
        
    }
}