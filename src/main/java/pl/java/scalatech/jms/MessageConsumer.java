package pl.java.scalatech.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import pl.java.scalatech.component.Counter;
@Slf4j
public class MessageConsumer implements MessageListener{

    @Autowired
    private Counter counter;
    
    @Override
    public void onMessage(Message message) {
             counter.getCountMessage().incrementAndGet();
             try {
                log.info("-- consumer --- {}",((TextMessage)message).getText());
            } catch (JMSException e) {
             
            }
        
    }

}
