package com.samuelji.fishgame.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samuelji.fishgame.service.FishService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/fish")
@AllArgsConstructor
public class FishCatchController {

    private FishService fishService;

    @GetMapping("/catch")
    public Map<String, Object> catchFish(@RequestParam String userId) {
        return fishService.catchFish(userId);
    }

}