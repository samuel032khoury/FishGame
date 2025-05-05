package com.samuelji.fishgame.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelji.fishgame.model.PurchasedItem;

public interface PurchasedItemRepository extends JpaRepository<PurchasedItem, Long> {
    Optional<PurchasedItem> findByUserIdAndShopItemId(String userId, Long shopItemId);
}
