package study.amqp.rabbit.example.async;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.amqp.rabbit.example.AppConfig;

@Configuration
public class AppConsumerConfig extends AppConfig {

    @Bean
    public SimpleMessageListenerContainer listenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(this.helloWorldQueueName);
        container.setMessageListener(new MessageListenerAdapter(new HelloWorldHandler()));
        return container;
    }

}
