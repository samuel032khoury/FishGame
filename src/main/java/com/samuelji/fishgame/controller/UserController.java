package com.samuelji.fishgame.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samuelji.fishgame.dto.TimestampTokenDTO;
import com.samuelji.fishgame.dto.UserDTO;
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
    public ResponseEntity<String> checkUserExist(@RequestParam String userId) {
        boolean exists = userService.userExists(userId);
        if (exists) {
            return ResponseEntity.ok("User exists");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    // Create user
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserDTO.Request request) {
        if (userService.userExists(request.getUserId())) {
            return ResponseEntity.status(409).body("User already exists");
        }
        userService.createUser(request);
        return ResponseEntity.status(201).body("User created successfully");
    }

    // Get user basic info
    @GetMapping("/basic")
    public ResponseEntity<?> getUserBasic(@RequestParam String userId) {
        if (!userService.userExists(userId)) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.ok(userService.getUserBasicInfo(userId));
    }

    // Get user finance info
    @GetMapping("/finance")
    public ResponseEntity<?> getUserFinance(@RequestParam String userId) {
        if (!userService.userExists(userId)) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.ok(userService.getUserFinanceInfo(userId));
    }

    // Get user level info
    @GetMapping("/level")
    public ResponseEntity<?> getUserLevel(@RequestParam String userId) {
        if (!userService.userExists(userId)) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.ok(userService.getUserLevelInfo(userId));
    }

    // Get user inventory info
    @GetMapping("/inventory")
    public ResponseEntity<?> getUserInventory(@RequestParam String userId) {
        if (!userService.userExists(userId)) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.ok(userService.getUserInventoryInfo(userId));
    }

    @GetMapping("/generate-token")
    public ResponseEntity<TimestampTokenDTO.Response> generateToken() {
        long timestamp = System.currentTimeMillis() / 1000;
        String token = generateToken(API_PASSWORD, String.valueOf(timestamp));
        TimestampTokenDTO.Response response = new TimestampTokenDTO.Response(token, String.valueOf(timestamp));
        return ResponseEntity.ok(response);
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
