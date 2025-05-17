package com.samuelji.fishgame.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samuelji.fishgame.model.Image;
import com.samuelji.fishgame.repository.ImageRepository;
import com.samuelji.fishgame.service.ImageGeneratorService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ImageController {
    private final ImageRepository imageRepository;
    private final ImageGeneratorService imageGeneratorService;

    @GetMapping("/check-usage")
    public ResponseEntity<Map<String, Object>> checkAndRegenerate() {
        List<Image> images = imageRepository.findAll();
        long used = images.stream().mapToLong(Image::getUsageCount).sum();
        long total = images.stream().mapToLong(Image::getTotalCount).sum();
        double ratio = total == 0 ? 0 : (double) used / total;

        if (ratio > 0.5) {
            imageGeneratorService.regenerateImages(10);
            return ResponseEntity.ok(Map.of("msg", String.format("Regenerated images, usage ratio: %.2f", ratio)));
        }
        return ResponseEntity.ok(Map.of("msg", String.format("Usage ratio: %.2f", ratio)));
    }
}
