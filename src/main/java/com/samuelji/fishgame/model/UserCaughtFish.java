package com.samuelji.fishgame.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class UserCaughtFish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "uid")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "fish_species_id")
    @JsonBackReference
    private FishSpecies fishSpecies;

    private String type;
    private Double price;
    private Double weight;
    private String url;
    private String description;
}