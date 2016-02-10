package pl.java.scalatech.camel;

import static org.apache.camel.component.jms.JmsComponent.jmsComponentClientAcknowledge;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class RequestReplyJmsTest extends CamelTestSupport {

    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        camelContext.addComponent("jms", jmsComponentClientAcknowledge(connectionFactory));

        return camelContext;
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("seda:test").inOut("jms:incomingOrders?useMessageIDAsCorrelationID=true&replyTo=validate");
                from("jms:validate").bean(ValidatorBean.class);
            }
        };
    }

    @Test
    public void testClientGetsReply() throws Exception {
        Object requestBody = template.requestBody("seda:test", "<order name=\"motor\" amount=\"1\" customer=\"honda\"/>");
        assertEquals("Valid", requestBody);
    }

}