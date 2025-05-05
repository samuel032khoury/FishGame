package com.samuelji.fishgame.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String SHOP_PURCHASE_QUEUE = "shop_purchase_queue";

    @Bean
    public Queue shopPurchaseQueue() {
        return new Queue(SHOP_PURCHASE_QUEUE, true);
    }
}
