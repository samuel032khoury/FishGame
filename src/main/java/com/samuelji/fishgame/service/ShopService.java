package com.samuelji.fishgame.service;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuelji.fishgame.config.RabbitMQConfig;
import com.samuelji.fishgame.dto.ShopItemDTO;
import com.samuelji.fishgame.message.PurchaseMessage;
import com.samuelji.fishgame.model.PurchasedItem;
import com.samuelji.fishgame.model.ShopItem;
import com.samuelji.fishgame.model.User;
import com.samuelji.fishgame.repository.PurchasedItemRepository;
import com.samuelji.fishgame.repository.ShopItemRepository;
import com.samuelji.fishgame.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ShopService {
    private final StringRedisTemplate redis;
    private final RabbitTemplate rabbit;
    private final ShopItemRepository shopItemRepository;
    private final PurchasedItemRepository purchasedItemRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    private class UserNotFoundException extends NotFoundException {
    }

    private class ItemNotFoundException extends NotFoundException {
    }

    @Transactional
    public ResponseEntity<String> purchaseItem(String userId, String itemName, String itemCategory) {
        String lockKey = String.format("shop_items_lock_%s_%s", userId, itemName);
        Boolean lockAcquired = redis.opsForValue().setIfAbsent(lockKey, "locked", 10, TimeUnit.SECONDS);
        if (lockAcquired == null || !lockAcquired) {
            return ResponseEntity.status(429).body("Too many requests, please try again later.");
        }
        try {
            User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
            String cacheKey = String.format("shop_item_%s_%s", itemName, itemCategory);
            String cachedValue = redis.opsForValue().get(cacheKey);
            ShopItem shopItem;
            if (cachedValue != null) {
                shopItem = objectMapper.readValue(cachedValue, ShopItem.class);
            } else {
                shopItem = shopItemRepository.findByNameAndCategory(itemName, itemCategory)
                        .orElseThrow(ItemNotFoundException::new);
                ShopItemDTO shopItemDTO = new ShopItemDTO(
                        shopItem.getId(),
                        shopItem.getName(),
                        shopItem.getDescription(),
                        shopItem.getCategory(), shopItem.getPrice());
                redis.opsForValue().set(cacheKey, objectMapper.writeValueAsString(shopItemDTO), 10, TimeUnit.MINUTES);
            }
            double price = shopItem.getPrice();
            if (user.getCoins() < price) {
                return ResponseEntity.badRequest().body("Not enough coins to purchase this item.");
            }

            PurchasedItem purchasedItem = purchasedItemRepository
                    .findByUserIdAndShopItemId(user.getId(), shopItem.getId())
                    .orElseGet(() -> {
                        PurchasedItem newPurchasedItem = new PurchasedItem();
                        newPurchasedItem.setUser(userRepository.getReferenceById(user.getId()));
                        newPurchasedItem.setShopItem(shopItemRepository.getReferenceById(shopItem.getId()));
                        newPurchasedItem.setQuantity(0);
                        return newPurchasedItem;
                    });
            purchasedItem.setQuantity(purchasedItem.getQuantity() + 1);
            purchasedItemRepository.save(purchasedItem);
            user.setCoins(user.getCoins() - (int) price);
            userRepository.save(user);

            PurchaseMessage purchaseMessage = new PurchaseMessage(userId, String.valueOf(shopItem.getId()), price);
            rabbit.convertAndSend(RabbitMQConfig.SHOP_PURCHASE_QUEUE, objectMapper.writeValueAsString(purchaseMessage));
            return ResponseEntity.ok(String.format(
                    "Successfully purchased %s from %s category for %d coins. You now have %d this item and %d coins.",
                    itemName, itemCategory, (int) price, purchasedItem.getQuantity(), user.getCoins()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(String.format("User %s Not Found", userId));
        } catch (ItemNotFoundException e) {
            return ResponseEntity.status(404).body(String.format("Item %s (Cat-%s) Not Found",
                    itemName, itemCategory));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        } finally {
            redis.delete(lockKey);
        }
    }

}
