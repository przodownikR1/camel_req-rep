package pl.java.scalatech.mock;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class HelloWorldRouteBuilderTest extends CamelTestSupport {

    @Produce(uri = "direct:in")
    ProducerTemplate in;

    @EndpointInject(uri = "mock:out")
    MockEndpoint out;

    @Test
    public void canSayHello() throws Exception {
        out.expectedBodiesReceived("Hello World!");
        in.sendBody("World");
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new HelloWorldRouteBuilder();
    }
}