package pl.java.scalatech;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.concurrent.Callable;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public final class Utils {
    public static Callable<Boolean> untilCheckEndpoint(ConsumerTemplate ct, String endpoint) {
        return () -> {
            Exchange ex = ct.receive(endpoint);
            log.info("retrieve message ---->  {}",ex.getIn().getBody());
            if (ex != null) {
                return TRUE;
            }
            return FALSE;
        };
    }
}
