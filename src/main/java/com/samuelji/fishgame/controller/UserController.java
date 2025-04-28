package com.samuelji.fishgame.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samuelji.fishgame.model.User;
import com.samuelji.fishgame.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private static final String API_PASSWORD = "default_password";

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

    @GetMapping("/generate-token")
    public Map<String, String> generateToken() {
        long timestamp = System.currentTimeMillis() / 1000;
        String token = generateToken(API_PASSWORD, String.valueOf(timestamp));

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("timestamp", String.valueOf(timestamp));
        return response;
    }

    private String generateToken(String password, String timestamp) {
        try {
            String data = password + timestamp + password;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }
}
