package com.samuelji.fishgame.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samuelji.fishgame.model.Fish;

@Repository
public interface FishRepository extends JpaRepository<Fish, Long> {
    List<Fish> findByUserId(String userId);

    List<Fish> findByUserIdAndType(String userId, String fishType, Pageable pageable);
}
