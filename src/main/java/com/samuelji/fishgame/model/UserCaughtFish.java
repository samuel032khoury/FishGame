package com.samuelji.fishgame.model;

import java.util.Optional;

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

    private Double weight;

    public String getRank() {
        if (weight > fishSpecies.getSWeight())
            return "SS";
        if (weight > fishSpecies.getAWeight())
            return "S";
        if (weight > fishSpecies.getBWeight())
            return "A";
        if (weight > fishSpecies.getCWeight())
            return "B";
        return "C";
    }

    public String getImageUrl() {
        return Optional.ofNullable(fishSpecies.getImages())
                .map(images -> images.get(getRank()))
                .orElse("https://s3.us-west-1.amazonaws.com/fishing.web.images/Fishing+Game+Images/Other/pearl.png");
    }
}