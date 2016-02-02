package pl.java.scalatech.mock;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;

public class HelloWorldRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:in")
                .process(exchange -> {
                    Message in = exchange.getIn();
                    in.setBody("Hello " + in.getBody(String.class) + "!");
                })
                .to("mock:out");
    }
}