package com.samuelji.fishgame.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    private String userName;
    private int coins;
    private int diamonds;
    private int level;
    private int currentExperience;
    private int experienceForNextLevel;
    private String rodType;

    @ElementCollection
    private Map<Long, Fish> fishInventory;

    @OneToMany(mappedBy = "user")
    private Set<PurchasedItem> purchasedItems = new HashSet<>();
}
