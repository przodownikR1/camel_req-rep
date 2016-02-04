package pl.java.scalatech.pure_spring;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.mockito.Mockito.verify;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import pl.java.scalatech.jms.MessageConsumer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doAnswer;

@Slf4j
@ContextConfiguration({ "classpath:springConfig.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringMessageTest {
    @Mock
    MessageConsumer messageConsumerMock;
    @Mock
    private JmsTemplate jmsTemplate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SneakyThrows
    public void sendMessage() throws JMSException {
        // doNothing().when(jmsTemplate).convertAndSend(Mockito.eq("queueA"), Mockito.any(Message.class));
        doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) {
                String result = (String) invocation.getArguments()[1];
                Assertions.assertThat(result).isEqualTo("test");

                return result;
            }
        }).when(jmsTemplate).convertAndSend(Mockito.eq("queueA"), Mockito.any(Message.class));
        jmsTemplate.convertAndSend("queueA", "test");
        verify(jmsTemplate).convertAndSend(Mockito.eq("queueA"), Mockito.any(Message.class));
        //Mockito.verify(messageConsumerMock).onMessage(Mockito.any(Message.class));

    }

}
