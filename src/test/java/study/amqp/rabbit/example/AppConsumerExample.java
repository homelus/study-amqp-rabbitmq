package study.amqp.rabbit.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class AppConsumerExample {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void consumer() {
        final AmqpTemplate amqpTemplate = applicationContext.getBean(AmqpTemplate.class);
        System.out.println("Received: " + amqpTemplate.receiveAndConvert());

    }


}
