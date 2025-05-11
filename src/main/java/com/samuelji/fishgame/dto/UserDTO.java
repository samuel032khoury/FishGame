package com.samuelji.fishgame.dto;

import java.util.List;

import com.samuelji.fishgame.model.UserCaughtFish;

import lombok.AllArgsConstructor;
import lombok.Data;

public class UserDTO {
    @Data
    public static class Request {
        private String userId;
        private String userName;
        private String email;
    }

    @Data
    @AllArgsConstructor
    public static class BasicInfoResponse {
        private String userName;
        private String rodType;
        private String email;
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
        private List<UserCaughtFish> fishInventory;
    }
}
