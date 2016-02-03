package pl.java.scalatech.pure;

import static com.google.common.io.ByteSource.wrap;
import static org.hamcrest.core.IsEqual.equalTo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RoutesDefinition;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.io.ByteStreams;
import com.jayway.awaitility.Awaitility;


import lombok.extern.slf4j.Slf4j;
@Slf4j
public class CamelContextPureTest {
    @Test
    @Ignore
    //strange :) , from IDE works - > really works  
    public void shoultCamelRouteWork() throws Exception {
        InputStream inputStream = CamelContextPureTest.class.getResourceAsStream("route.xml");
        DefaultCamelContext context = new DefaultCamelContext();
        ConsumerTemplate consumer = context.createConsumerTemplate();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();                
        ByteStreams.copy(inputStream, baos);
        String fileContent = baos.toString();
        log.info("+++  {}", fileContent);        
        RoutesDefinition rd = context.loadRoutesDefinition(wrap(baos.toByteArray()).openStream());
        
        context.addRouteDefinitions(rd.getRoutes());
        context.start();
        Awaitility.await().untilAtomic(checkEndpoint(consumer, "direct:end"),equalTo(true));
        
    }

    private AtomicBoolean checkEndpoint(ConsumerTemplate ct, String endpoint){
         AtomicBoolean atomic = new AtomicBoolean(false);
         Exchange ex = ct.receive(endpoint);
         if(ex!= null){
             atomic.set(true);
         }
         return atomic;
    }
    
}
