package com.samuelji.fishgame.dto;

import java.util.HashMap;
import java.util.Map;

import com.samuelji.fishgame.model.Fish;

import lombok.AllArgsConstructor;
import lombok.Data;

public class UserDTO {
    @Data
    public static class Request {
        private String userId;
        private String userName;
        private int coins = 0;
        private int diamonds = 0;
        private int level = 1;
        private int currentExperience = 0;
        private int experienceForNextLevel = 100;
        private String rodType = "Basic";
        private Map<Long, Fish> fishInventory = new HashMap<>();
    }

    @Data
    @AllArgsConstructor
    public static class BasicInfoResponse {
        private String userName;
        private String rodType;
    }

    @Data
    @AllArgsConstructor
    public static class FinanceInfoResponse {
        private int coins;
        private int diamonds;
    }

    @Data
    @AllArgsConstructor
    public static class LevelInfoResponse {
        private int level;
        private int currentExperience;
        private int experienceForNextLevel;
    }

    @Data
    @AllArgsConstructor
    public static class InventoryInfoResponse {
        private Map<Long, Fish> fishInventory;
    }
}
