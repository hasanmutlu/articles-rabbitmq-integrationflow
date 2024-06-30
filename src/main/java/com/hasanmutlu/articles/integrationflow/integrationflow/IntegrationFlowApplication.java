package com.hasanmutlu.articles.integrationflow.integrationflow;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IntegrationFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegrationFlowApplication.class, args);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate logWriterQueueTemplate(ConnectionFactory connectionFactory,
                                               MessageConverter messageConverter,
                                               @Value("${messaging.log.writer.queue}") String name) {
        return createQueue(connectionFactory,messageConverter, name);
    }


    private AmqpTemplate createQueue(ConnectionFactory connectionFactory,MessageConverter messageConverter, String queueName) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setRoutingKey(queueName);
        return rabbitTemplate;


    }

}
