package com.samuelji.fishgame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelji.fishgame.model.FishSpecies;

public interface FishSpeciesRepository extends JpaRepository<FishSpecies, Long> {
    List<FishSpecies> findByStatusTrue();
}