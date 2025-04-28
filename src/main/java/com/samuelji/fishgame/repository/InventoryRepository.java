package com.samuelji.fishgame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelji.fishgame.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByUserId(String userId);
}
