package com.samuelji.fishgame.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samuelji.fishgame.model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByUserId(String userId);

    List<Inventory> findByUserIdAndType(String userId, String fishType, Pageable pageable);
}
