package com.hasanmutlu.articles.integrationflow.integrationflow.queue;

import com.hasanmutlu.articles.integrationflow.integrationflow.dto.LogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class LogWriterQueueHandlerByType implements CorrelationStrategy {

    @Value("${messaging.log.writer.queue}")
    private String QUEUE_NAME;

    private final MessageConverter messageConverter;

    private final boolean QUEUE_AUTO_START = true;

    private final int BATCH_SIZE = 5;

    private final int BATCH_TIMEOUT = 1000;

    @Bean
    public IntegrationFlow logFlowByType(ConnectionFactory connectionFactory) {
        return IntegrationFlow.from(
                        Amqp.inboundAdapter(connectionFactory, QUEUE_NAME) // listen to the queue
                                .messageConverter(messageConverter)
                                .autoStartup(QUEUE_AUTO_START))

                .aggregate(a -> a
                        .correlationStrategy(this, "getCorrelationKey")
                        .releaseStrategy(r -> r.size() == BATCH_SIZE) // complete the group after BATCH SIZE of messages
                        .expireGroupsUponCompletion(true) // don't wait to finish the group
                        .sendPartialResultOnExpiry(true) // send partial results if needed
                        .groupTimeout(BATCH_TIMEOUT)) // timeout groups after milliseconds
                .handle(this) // calls handleMessage method
                .get();
    }


    @Override
    public Object getCorrelationKey(Message<?> message) {
        // Integration flow is not allowed null values, so we need to set a default value
        if (message.getPayload() instanceof LogDto) {
            var type = ((LogDto) message.getPayload()).getType();
            return Objects.requireNonNullElse(type, -1);
        } else {
            throw new RuntimeException("Correlation has been failed");
        }
    }

    @ServiceActivator
    public void handleMessage(Collection<LogDto> aggregatedData) {
        System.out.println(MessageFormat.format("Collected Items {0}", aggregatedData));
    }
}
