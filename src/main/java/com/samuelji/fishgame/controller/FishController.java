package com.samuelji.fishgame.controller;

import java.util.Map;

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
    public Map<String, Object> catchFish(@RequestParam String userId) {
        return fishService.catchFish(userId);
    }

    @PostMapping("/sellAll")
    public Map<String, Object> sellFish(@RequestParam String userId) {
        return fishService.sellFish(userId);
    }

    @PostMapping("/sell")
    public Map<String, Object> sellFishByType(@RequestParam String userId, @RequestParam String fishType,
            @RequestParam int amount) {
        return fishService.sellFishByType(userId, fishType, amount);
    }

}