package com.samuelji.fishgame.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Fish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String description;
    private Double probability;
    @JsonProperty("sWeight")
    private Double sWeight;
    @JsonProperty("aWeight")
    private Double aWeight;
    @JsonProperty("bWeight")
    private Double bWeight;
    @JsonProperty("cWeight")
    private Double cWeight;
    private Double mean;
    private Double standardDeviation = 0.625;
    private Boolean status = true;
}
