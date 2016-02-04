package pl.java.scalatech.longRunning;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import lombok.SneakyThrows;
import pl.java.scalatech.CamelHelperTest;

public class LongRunningTaskTest extends CamelHelperTest {

    @Override
    protected String setContextResourceFile() {
        return "longRunning.xml";
    }
    
    
    @Produce(uri = "seda:in")
    ProducerTemplate in;

    @EndpointInject(uri = "mock:out")
    MockEndpoint out;
    @Before
    @SneakyThrows
    public void before(){
       
        String join_longRunning = context.resolvePropertyPlaceholders("{{join.longRunning}}");
        log.info("++ {}",join_longRunning);
    
    }
    
    @Test
   
    public void should_long_running_task_work() throws Exception {        
        List<String> result = Lists.newArrayList("1olleH","2olleH","3olleH","4olleH","5olleH"); 
        out.expectedBodiesReceivedInAnyOrder(result);
        out.expectedMessageCount(5);
       
        IntStream.rangeClosed(1, 5).forEach(l ->  in.sendBodyAndHeader("Hello"+l,"PRIORITY",""+l));
         
        assertMockEndpointsSatisfied();
        List<Exchange> list = out.getReceivedExchanges();
        Assertions.assertThat(list.get(0).getIn().getBody(String.class)).contains("olleH");
        log.info(" +++ header {} , body {}",list.get(0).getIn().getHeader("PRIORITY"),list.get(0).getIn().getBody(String.class));
        out.allMessages().body().contains("olleH");
        
        
        out.message(0).header("PRIORITY").isGreaterThan(0);
        log.info("xxxx  {}",out.message(0).headers());
        out.expectsDescending(header("PRIORITY"));
        
    }

}
