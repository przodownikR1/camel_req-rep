package pl.java.scalatech.camel;

import static org.apache.camel.component.jms.JmsComponent.jmsComponentClientAcknowledge;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.interceptor.DefaultTraceFormatter;
import org.apache.camel.processor.interceptor.Tracer;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class RequestReplyJmsTest extends CamelTestSupport {

    protected CamelContext createCamelContext() throws Exception {        
        CamelContext camelContext = super.createCamelContext();
        tracerSettings(camelContext);            
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        camelContext.addComponent("jms", jmsComponentClientAcknowledge(connectionFactory));
        return camelContext;
    }

    private void tracerSettings(CamelContext camelContext) {
        Tracer tracer = new Tracer();
        tracer.setTraceExceptions(true);
        tracer.setTraceInterceptors(true);
        tracer.setLogLevel(LoggingLevel.TRACE);
        tracer.setLogName("camel");
        camelContext.setTracing(true);
        DefaultTraceFormatter tf= new DefaultTraceFormatter();
        tf.setShowHeaders(true);
        tf.setShowBody(true);
        tf.setShowOutBody(true);
        tf.setShowOutHeaders(true);
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("jms:incomingOrders").inOut("jms:validate");        
                from("jms:validate").bean(ValidatorBean.class);
            }
        };
    }

    @Test
    public void testClientGetsReply() throws Exception {
        Object requestBody = template.requestBody("jms:incomingOrders", "<order name=\"motor\" amount=\"1\" customer=\"honda\"/>");
        assertEquals("Valid", requestBody);
    }

}