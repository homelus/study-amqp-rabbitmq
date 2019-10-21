package study.amqp.rabbit.example.async;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import study.amqp.rabbit.example.AppConfig;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableScheduling
public class AppProducerConfig extends AppConfig {

    @Bean
    public ScheduledProducer producer() {
        return new ScheduledProducer();
    }

    public static class ScheduledProducer {

        @Autowired
        private AmqpTemplate amqpTemplate;

        private AtomicInteger count = new AtomicInteger();

        @Scheduled(fixedRate = 2_000)
        public void scheduling() {
            amqpTemplate.convertAndSend("hello " + count.incrementAndGet());
            System.out.println("send " + count.get());
        }

    }

}
