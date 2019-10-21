package study.amqp.rabbit.example.async;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppProducerConfig.class)
public class AsyncAppProducerExample {

    @Test
    public void producer() throws InterruptedException {
        TimeUnit.SECONDS.sleep(60);
    }



}
