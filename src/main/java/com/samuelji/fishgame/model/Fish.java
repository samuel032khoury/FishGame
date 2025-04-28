package com.samuelji.fishgame.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Fish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // Fish type
    private String description; // Fish description
    private Double probability; // Probability of catching this fish
    @JsonProperty("sWeight")
    private Double sWeight; // threshold weight to be considered legendary
    @JsonProperty("aWeight")
    private Double aWeight; // threshold weight to be considered rare
    @JsonProperty("bWeight")
    private Double bWeight; // threshold weight to be considered uncommon
    @JsonProperty("cWeight")
    private Double cWeight; // threshold weight to be considered common
    private Double mean; // Mean weight of the fish
    private Double standardDeviation; // Standard deviation of the fish weight
    private Boolean status; // Status of the fish (availability for catching)
}
