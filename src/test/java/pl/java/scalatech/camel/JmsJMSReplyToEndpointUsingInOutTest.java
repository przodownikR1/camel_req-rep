package pl.java.scalatech.camel;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.apache.camel.component.jms.JmsComponent.jmsComponentAutoAcknowledge;

import javax.jms.ConnectionFactory;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class JmsJMSReplyToEndpointUsingInOutTest extends CamelTestSupport {
    private JmsComponent amq;

    @Test
    public void testCustomJMSReplyToInOut() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived("My name is Slawek");

        newFixedThreadPool(1).submit(() -> {
            JmsTemplate jms = new JmsTemplate(amq.getConfiguration().getConnectionFactory());
            final TextMessage msg = (TextMessage) jms.receive("serviceRequestor");
            assertEquals("What's your name", msg.getText());            
            jms.send(msg.getJMSReplyTo(), (MessageCreator) session -> {
                TextMessage replyMsg = session.createTextMessage();
                replyMsg.setText("My name is Slawek");
                replyMsg.setJMSCorrelationID(msg.getJMSCorrelationID());
                return replyMsg;
            });
            return null;
        });
       
        String response = template.requestBody("activemq:queue:hello","What's your name",String.class);
        Assertions.assertThat(response).isEqualTo("My name is Slawek");          
        assertMockEndpointsSatisfied();
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            JmsTemplate jms = new JmsTemplate(amq.getConfiguration().getConnectionFactory());
            public void configure() throws Exception {
                from("activemq:queue:hello")
                    .to(ExchangePattern.InOut, "activemq:queue:serviceRequestor?replyTo=queue:serviceReplyQueue")
                    .to("mock:result");                                               
            }
        };
    }
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        camelContext.addComponent("activemq", jmsComponentAutoAcknowledge(connectionFactory));
        amq = camelContext.getComponent("activemq", JmsComponent.class);
        return camelContext;
    }
    /*JmsTemplate jms = new JmsTemplate(amq.getConfiguration().getConnectionFactory());
    jms.send("hello", (MessageCreator) session -> {
        TextMessage msg = session.createTextMessage();
        msg.setText("What's your name");
        return msg;
    });*/
}