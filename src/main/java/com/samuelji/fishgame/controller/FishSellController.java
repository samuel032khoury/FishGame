package com.samuelji.fishgame.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samuelji.fishgame.service.FishService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/fish")
@AllArgsConstructor
public class FishSellController {
    private FishService fishService;

    @PostMapping("/sell")
    public Map<String, Object> sellFish(@RequestParam String userId) {
        return fishService.sellFish(userId);
    }
}
