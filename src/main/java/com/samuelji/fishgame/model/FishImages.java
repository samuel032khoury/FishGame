package com.samuelji.fishgame.model;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class FishImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

    @ElementCollection
    @CollectionTable(name = "fish_image_map")
    private Map<String, String> images = new HashMap<>();
}
