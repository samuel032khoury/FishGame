package com.samuelji.fishgame.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    private String userName;

    private int coins = 0;
    private int diamonds = 0;
    private int level = 1;
    private int currentExperience = 0;
    private int experienceForNextLevel = 100;
    private String rodType = "Basic";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UserCaughtFish> fishInventory = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<PurchasedItem> purchasedItems = new HashSet<>();
}
