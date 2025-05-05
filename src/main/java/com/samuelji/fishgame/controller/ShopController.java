package com.samuelji.fishgame.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samuelji.fishgame.service.ShopService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/shop")
@AllArgsConstructor
public class ShopController {

    private ShopService shopService;

    @GetMapping
    public String shopPage() {
        return "Welcome to the Shop!";
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseItem(@RequestParam String userId, @RequestParam String itemName,
            @RequestParam String itemCategory) {
        return shopService.purchaseItem(userId, itemName, itemCategory);
    }
}