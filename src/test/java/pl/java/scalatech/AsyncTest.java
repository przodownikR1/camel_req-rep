package pl.java.scalatech;

import static com.jayway.awaitility.Awaitility.await;
import static java.lang.Boolean.TRUE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.equalTo;
import static pl.java.scalatech.Utils.untilCheckEndpoint;

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
@ContextConfiguration({ "classpath:jmsConfigTest.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class AsyncTest {

    @Autowired
    private CamelContext context;
    @Autowired
    private ConsumerTemplate consumer;
    @Autowired
    private ProducerTemplate producer;

    @Test
    public void shouldPutAndGetMessageFromJms() throws Exception {
        Assertions.assertThat(context).isNotNull();
        Assertions.assertThat(consumer).isNotNull();
        Assertions.assertThat(producer).isNotNull();
        producer.sendBody("seda:in", "sdd");

        await().atMost(1, SECONDS).until(untilCheckEndpoint(consumer, "seda:out"), equalTo(TRUE));

    }

}
