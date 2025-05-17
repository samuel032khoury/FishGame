package com.samuelji.fishgame.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long usageCount = 0L;
    private Long totalCount = 0L;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String imageDescription;
    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    public Image(String imageDescription, String imageUrl) {
        this.imageDescription = imageDescription;
        this.imageUrl = imageUrl;
    }

    @Transient
    public double getUsageRatio() {
        return totalCount == 0 ? 0 : (double) usageCount / totalCount;
    }
}
