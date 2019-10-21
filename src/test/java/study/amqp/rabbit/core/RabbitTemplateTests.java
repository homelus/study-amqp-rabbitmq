package study.amqp.rabbit.core;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.ExecutorService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class RabbitTemplateTests {

    @Test
    public void returnConnectionAfterCommit() throws Exception {
        @SuppressWarnings("serial")
        TransactionTemplate txTemplate = new TransactionTemplate(new AbstractPlatformTransactionManager() {

            @Override
            protected Object doGetTransaction() throws TransactionException {
                return new Object();
            }

            @Override
            protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
            }

            @Override
            protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
            }

            @Override
            protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
            }
        });
        ConnectionFactory mockConnectionFactory = mock(ConnectionFactory.class);
        Connection mockConnection = mock(Connection.class);
        Channel mockChannel = mock(Channel.class);

        given(mockConnectionFactory.newConnection(any(ExecutorService.class), anyString())).willReturn(mockConnection);
        given(mockConnection.isOpen()).willReturn(true);
        given(mockConnection.createChannel()).willReturn(mockChannel);

        given(mockChannel.isOpen()).willReturn(true);

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(mockConnectionFactory);
        connectionFactory.setExecutor(mock(ExecutorService.class));
        final RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setChannelTransacted(true);

        txTemplate.execute(status -> {
            template.convertAndSend("foo", "bar");
            return null;
        });
        txTemplate.execute(status -> {
            template.convertAndSend("baz", "qux");
            return null;
        });
        verify(mockConnectionFactory, Mockito.times(1)).newConnection(any(ExecutorService.class), anyString());
        // ensure we used the same channel
        verify(mockConnection, times(1)).createChannel();
    }


}
