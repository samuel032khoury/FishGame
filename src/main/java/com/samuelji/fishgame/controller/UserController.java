package com.samuelji.fishgame.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samuelji.fishgame.model.User;
import com.samuelji.fishgame.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Check if user exists
    @GetMapping("/is-exist")
    public Map<String, Object> checkUserExist(@RequestParam String userId) {
        boolean exists = userService.userExists(userId);
        return Map.of("code", exists ? 200 : 404, "msg", exists ? "User exists" : "User not found");
    }

    // Create user
    @PostMapping("/create")
    public Map<String, Object> createUser(@RequestBody User user) {
        if (userService.userExists(user.getUserId())) {
            return Map.of("code", 409, "msg", "User already exists");
        }
        userService.createUser(user);
        return Map.of("code", 200, "msg", "User created successfully");
    }

    // Get user basic info
    @GetMapping("/basic")
    public Map<String, Object> getUserBasic(@RequestParam String userId) {
        return userService.getUserBasicInfo(userId);
    }

    // Get user finance info
    @GetMapping("/finance")
    public Map<String, Object> getUserFinance(@RequestParam String userId) {
        return userService.getUserFinanceInfo(userId);
    }
}
