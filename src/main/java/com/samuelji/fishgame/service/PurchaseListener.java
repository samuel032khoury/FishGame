package com.samuelji.fishgame.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.samuelji.fishgame.config.RabbitMQConfig;
import com.samuelji.fishgame.message.PurchaseMessage;
import com.samuelji.fishgame.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseListener {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void onMessage(PurchaseMessage message) {
        try {
            String userEmail = lookupUserEmail(message.getUserId());
            String username = lookUpUserName(message.getUserId());
            Integer userBalance = lookUpUserBalance(message.getUserId());
            if (userEmail != null) {
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setTo(userEmail);
                mail.setFrom("x.samuel.j032@gmail.com");
                mail.setSubject("Purchase Confirmation");
                mail.setText(String.format(
                        "Dear %s,\n\nYou have successfully purchased %s in the category %s.\n\n Your current balance is %d.\n\nThank you!",
                        username, message.getItemName(), message.getCategory(), userBalance));
                mailSender.send(mail);
            }
        } catch (Exception e) {
            return;
        }
    }

    private Integer lookUpUserBalance(String userId) {
        return userRepository.findByUserId(userId).map(item -> item.getCoins()).orElse(0);
    }

    private String lookUpUserName(String userId) {
        return userRepository.findByUserId(userId).map(item -> item.getUserName()).orElse("valued player");
    }

    private String lookupUserEmail(String userId) {
        return userRepository.findByUserId(userId).map(item -> item.getEmail()).orElse(null);
    }
}
