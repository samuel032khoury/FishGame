package com.samuelji.fishgame.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelji.fishgame.model.FishImages;

public interface FishImagesRepository extends JpaRepository<FishImages, Long> {
    Optional<FishImages> findByType(String type);
}
