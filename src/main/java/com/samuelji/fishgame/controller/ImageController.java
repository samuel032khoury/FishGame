package com.samuelji.fishgame.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samuelji.fishgame.model.Image;
import com.samuelji.fishgame.repository.ImageRepository;
import com.samuelji.fishgame.service.ImageGenService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/image")
@AllArgsConstructor
public class ImageController {
    private final ImageRepository imageRepository;
    private final ImageGenService imageGenService;

    @GetMapping("/check-usage")
    public ResponseEntity<String> checkAndRegenerate() {
        List<Image> images = imageRepository.findAll();
        long used = images.stream().mapToLong(Image::getUsageCount).sum();
        long total = images.stream().mapToLong(Image::getTotalCount).sum();
        double ratio = total == 0 ? 0 : (double) used / total;

        // FIXME: count needs to be updated in Image model
        if (ratio > 0.5) {
            imageGenService.regenerateImages(10);
            return ResponseEntity
                    .ok(String.format("Usage ratio exceeds threshold (%.2f), regenerating images...", ratio));
        }
        return ResponseEntity.ok(String.format("Usage ratio: %.2f", ratio));
    }
}
