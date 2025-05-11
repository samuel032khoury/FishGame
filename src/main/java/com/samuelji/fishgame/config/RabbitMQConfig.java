package com.samuelji.fishgame.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "purchase.exchange";
    public static final String QUEUE = "purchase.queue";
    public static final String ROUTING = "purchase.routing";

    @Bean
    public DirectExchange purchaseExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue purchaseQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding binding(Queue purchaseQueue, DirectExchange purchaseExchange) {
        return BindingBuilder.bind(purchaseQueue).to(purchaseExchange).with(ROUTING);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
