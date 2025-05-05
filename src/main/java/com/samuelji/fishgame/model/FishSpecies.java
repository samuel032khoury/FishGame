package com.samuelji.fishgame.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class FishSpecies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // Type of fish
    private String description; // Description of this type
    private Double probability; // Probability of catching this type of fish
    @JsonProperty("sWeight")
    private Double sWeight; // Threshold weight to be considered legendary
    @JsonProperty("aWeight")
    private Double aWeight; // Threshold weight to be considered rare
    @JsonProperty("bWeight")
    private Double bWeight; // Threshold weight to be considered uncommon
    @JsonProperty("cWeight")
    private Double cWeight; // Threshold weight to be considered common
    private Double mean; // Mean weight of the fish
    private Double standardDeviation; // Standard deviation of the fish weight
    private Boolean status; // Status of the fish (availability for catching)
    private Double price; // Price of the fish
    private Double minWeight; // Minimum weight of the fish

    @ElementCollection
    @CollectionTable(name = "fish_image_map")
    private final Map<String, String> images = new HashMap<>();

    @OneToMany(mappedBy = "fishSpecies")
    @JsonManagedReference
    private List<UserCaughtFish> userCaughtFish = new java.util.ArrayList<>();

    public void setImage(String rank, String imageUrl) {
        images.put(rank, imageUrl);
    }
}
