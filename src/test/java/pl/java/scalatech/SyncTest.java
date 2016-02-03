package pl.java.scalatech;

import org.apache.camel.ExchangePattern;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SyncTest extends CamelHelperTest {

    @Override
    protected String setContextResourceFile() {
        return "jmsReplyRequest.xml";
    }

    @Test
    public void should_rr_work() {

        String response = (String) producer.sendBody("seda:start", ExchangePattern.InOut, "slawek");
        Assertions.assertThat(response).isEqualTo("kewals");
    }

}
