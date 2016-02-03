package pl.java.scalatech.mock;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

import pl.java.scalatech.CamelHelperTest;

public class InOutMockTest extends CamelHelperTest {

    @Produce(uri = "seda:in")
    ProducerTemplate in;

    @EndpointInject(uri = "mock:out")
    MockEndpoint out;

    @Test
    public void canSayHello() throws Exception {
        out.expectedBodiesReceived("olleH");
        in.sendBody("Hello");
        assertMockEndpointsSatisfied();
    }

    @Override
    protected String setContextResourceFile() {
        return "sedaInOutTest.xml";
    }
}
