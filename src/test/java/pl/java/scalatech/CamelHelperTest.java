package pl.java.scalatech;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class CamelHelperTest extends CamelTestSupport{
    
   
    protected ProducerTemplate producer;
    
    protected abstract String setContextResourceFile();
    
    @Override
    protected CamelContext createCamelContext() throws Exception {
         checkArgument(setContextResourceFile().contains(".xml"));
         ApplicationContext appContext = new ClassPathXmlApplicationContext(setContextResourceFile());
          
         CamelContext context=  appContext.getBean("camelContext", DefaultCamelContext.class);
         this.consumer = context.createConsumerTemplate();
         this.producer = context.createProducerTemplate();
         return context;
        
    }
}
