package com.samuelji.fishgame.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.samuelji.fishgame.config.RabbitMQConfig;
import com.samuelji.fishgame.message.PurchaseMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendPurchaseMessage(String userId, String itemName, String category) {
        PurchaseMessage msg = new PurchaseMessage();
        msg.setUserId(userId);
        msg.setItemName(itemName);
        msg.setCategory(category);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING,
                msg);
    }
}
