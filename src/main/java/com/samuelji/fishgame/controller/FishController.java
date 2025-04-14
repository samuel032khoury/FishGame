package com.samuelji.fishgame.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fish")
public class FishController {
    @GetMapping
    public String fishInfo() {
        return "Fish Information";
    }
}