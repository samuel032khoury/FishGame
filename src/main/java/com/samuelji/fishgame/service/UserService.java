package com.samuelji.fishgame.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.samuelji.fishgame.model.User;
import com.samuelji.fishgame.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean userExists(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    public void createUser(User user) {
        user.setCoins(0);
        user.setDiamonds(0);
        user.setLevel(1);
        user.setCurrentExperience(0);
        user.setCurrentExperience(100);
        user.setRodType("Basic");
        userRepository.save(user);
    }

    public Map<String, Object> getUserBasicInfo(String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            return Map.of("code", 404, "msg", "User not found");
        }
        return Map.of("code", 200, "msg", "Success", "data", Map.of(
                "userName", user.get().getUserName(),
                "rodType", user.get().getRodType()));
    }

    public Map<String, Object> getUserFinanceInfo(String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            return Map.of("code", 404, "msg", "User not found");
        }
        return Map.of("code", 200, "msg", "Success", "data", Map.of(
                "coins", user.get().getCoins(),
                "diamonds", user.get().getDiamonds()));
    }

    public Map<String, Object> getUserLevelInfo(String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            return Map.of("code", 404, "msg", "User not found");
        }
        return Map.of("code", 200, "msg", "Success", "data", Map.of(
                "level", user.get().getLevel(),
                "currentExperience", user.get().getCurrentExperience(),
                "experienceForNextLevel", user.get().getExperienceForNextLevel()));
    }

    public Map<String, Object> getUserInventoryInfo(String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            return Map.of("code", 404, "msg", "User not found");
        }
        return Map.of("code", 200, "msg", "Success", "data", Map.of(
                "fishInventory", user.get().getFishInventory()));
    }
}
