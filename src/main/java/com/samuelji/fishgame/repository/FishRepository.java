package com.samuelji.fishgame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelji.fishgame.model.Fish;

public interface FishRepository extends JpaRepository<Fish, Long> {
    List<Fish> findByStatusTrue();
}
