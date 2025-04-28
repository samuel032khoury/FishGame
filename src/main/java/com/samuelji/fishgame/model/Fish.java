package com.samuelji.fishgame.model;

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
    private Double sWeight;
    private Double aWeight;
    private Double bWeight;
    private Double cWeight;
    private Double mean;
    private Double standardDeviation = 0.625;
    private Boolean status = true;
}
