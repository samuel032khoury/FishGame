package com.samuelji.fishgame.controller;

import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samuelji.fishgame.service.FishService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/fish")
@AllArgsConstructor
public class FishController {

    private final FishService fishService;

    @GetMapping
    public String fishInfo() {
        return "Fish Information";
    }

    @PostMapping("/catch")
    public ResponseEntity<?> catchFish(@RequestParam String userId) {
        try {
            return ResponseEntity.ok(fishService.catchFish(userId));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/sellAll")
    public ResponseEntity<?> sellAllFish(@RequestParam String userId) {
        try {
            int revenue = fishService.sellAllFish(userId);
            return ResponseEntity.ok(Map.of("revenue", revenue));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellFishByType(@RequestParam String userId, @RequestParam String fishType,
            @RequestParam int amount) {
        try {
            int revenue = fishService.sellFishByType(userId, fishType, amount);
            return ResponseEntity.ok(Map.of("revenue", revenue));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}