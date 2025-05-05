package com.samuelji.fishgame.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelji.fishgame.model.ShopItem;

public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
    Optional<ShopItem> findByNameAndCategory(String name, String category);
}
