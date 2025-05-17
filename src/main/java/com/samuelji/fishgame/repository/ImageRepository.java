package com.samuelji.fishgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelji.fishgame.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}