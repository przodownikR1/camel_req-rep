package pl.java.scalatech;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ContextConfiguration({"classpath:jmsConfigTest.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class AsyncTest extends CamelTestSupport {
   

    @Test
    public void testActiveMQ() throws Exception{
        ProducerTemplate template = context.createProducerTemplate();
        
        log.info("+++                                            send message");
        template.sendBody("seda:test", "HelloWorld");
        Thread.sleep(2000);
       
        
    }

 


}
