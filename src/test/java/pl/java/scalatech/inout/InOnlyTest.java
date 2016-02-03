package pl.java.scalatech.inout;

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
@ContextConfiguration({ "classpath:sedaInOut.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class InOnlyTest {
    private final String NAME = "slawek borowiec";
    private final String NAME_RESULT = "slawek borowiec";
    
    @Autowired
    private CamelContext context;
    @Autowired
    private ConsumerTemplate consumer;
    @Autowired
    private ProducerTemplate producer;

    @Test
    public void should_return_the_same_string_as_put_in_start_route() throws Exception {
         
   
        Assertions.assertThat(context).isNotNull();
        Assertions.assertThat(consumer).isNotNull();
        Assertions.assertThat(producer).isNotNull();
        String result = producer.requestBody("seda:in", NAME,String.class);
        Assertions.assertThat(result).isEqualTo(NAME_RESULT);

        //await().atMost(2, SECONDS).until(untilCheckEndpoint(consumer, "seda:out"), equalTo(TRUE));

    }
}