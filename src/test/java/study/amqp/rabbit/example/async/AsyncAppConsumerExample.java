package study.amqp.rabbit.example.async;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import study.amqp.rabbit.example.AppConfig;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConsumerConfig.class)
public class AsyncAppConsumerExample {

    @Test
    public void consumer() throws InterruptedException {
        TimeUnit.SECONDS.sleep(60);
    }


}
