package study.amqp.rabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness.InvocationData;
import org.springframework.amqp.rabbit.test.mockito.LatchCountDownAndCallRealMethodAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

/**
 * @author playjun
 * @since 2019 10 18
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class RabbitListenerSpyAndCaptureTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue1;

    @Autowired
    private Queue queue2;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void testOneWay() throws InterruptedException {
        Listener listener = this.harness.getSpy("bar");
        assertNotNull(listener);

        LatchCountDownAndCallRealMethodAnswer answer = new LatchCountDownAndCallRealMethodAnswer(2);
        doAnswer(answer).when(listener).foo(anyString(), anyString());

        this.rabbitTemplate.convertAndSend(this.queue2.getName(), "bar");
        this.rabbitTemplate.convertAndSend(this.queue2.getName(), "baz");
        this.rabbitTemplate.convertAndSend(this.queue2.getName(), "ex");

        assertTrue(answer.getLatch().await(10, TimeUnit.SECONDS));
        verify(listener).foo("bar", this.queue2.getName());
        verify(listener).foo("baz", this.queue2.getName());

        InvocationData invocationData = this.harness.getNextInvocationDataFor("bar", 10, TimeUnit.SECONDS);
        assertNotNull(invocationData);

        Object[] args = invocationData.getArguments();
        assertThat((String) args[0], equalTo("bar"));
        assertThat((String) args[1], equalTo(queue2.getName()));

        invocationData = this.harness.getNextInvocationDataFor("bar", 10, TimeUnit.SECONDS);
        assertNotNull(invocationData);
        args = invocationData.getArguments();
        assertThat((String) args[0], equalTo("baz"));
        assertThat((String) args[1], equalTo(queue2.getName()));

        invocationData = this.harness.getNextInvocationDataFor("bar", 10, TimeUnit.SECONDS);
        assertNotNull(invocationData);
        args = invocationData.getArguments();
        assertThat((String) args[0], equalTo("ex"));
        assertThat((String) args[1], equalTo(queue2.getName()));
        assertNotNull(invocationData.getThrowable());
        assertEquals("ex", invocationData.getThrowable().getMessage());

        invocationData = this.harness.getNextInvocationDataFor("bar", 10, TimeUnit.SECONDS);
        assertNotNull(invocationData);
        args = invocationData.getArguments();
        assertThat((String) args[0], equalTo("ex"));
        assertThat((String) args[1], equalTo(queue2.getName()));
        assertNull(invocationData.getThrowable());
    }

    @Configuration
    @RabbitListenerTest(capture = true)
    public static class Config {

        @Bean
        public Listener listener() {
            return new Listener();
        }

        @Bean
        public ConnectionFactory connectionFactory() {
            CachingConnectionFactory factory = new CachingConnectionFactory("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");
            return factory;
        }

        @Bean
        public Queue queue1() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue queue2() {
            return new AnonymousQueue();
        }

        @Bean
        public RabbitAdmin admin(ConnectionFactory connectionFactory) {
            return new RabbitAdmin(connectionFactory);
        }

        @Bean
        public RabbitTemplate template(ConnectionFactory connectionFactory) {
            return new RabbitTemplate(connectionFactory);
        }

        @Bean
        public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
            SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
            containerFactory.setConnectionFactory(connectionFactory);
            return containerFactory;
        }

    }

    public static class Listener {

        private boolean failed;

        @RabbitListener(id = "foo", queues = "#{queue1.name}")
        public String foo(String foo) {
            return foo.toUpperCase();
        }

        @RabbitListener(id = "bar", queues = "#{queue2.name}")
        public void foo(@Payload String foo, @Header("amqp_receivedRoutingKey") String rk) {
            System.out.println("listener: " + foo);
            if (!failed && foo.equals("ex")) {
                failed = true;
                throw new RuntimeException(foo);
            }
            failed = false;
        }

    }

}
