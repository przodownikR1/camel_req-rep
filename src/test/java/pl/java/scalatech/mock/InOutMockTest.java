package pl.java.scalatech.mock;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InOutMockTest extends CamelTestSupport {
       
        @Produce(uri = "seda:in")
        ProducerTemplate in;

        @EndpointInject(uri = "mock:out")
        MockEndpoint out;

        @Test
        public void canSayHello() throws Exception {
            out.expectedBodiesReceived("olleH");
            in.sendBody("Hello");
            assertMockEndpointsSatisfied();
        }

       
        @Override
        protected CamelContext createCamelContext() throws Exception {
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("sedaInOutTest.xml");
            return SpringCamelContext.springCamelContext(applicationContext);
        }
        
       
}
