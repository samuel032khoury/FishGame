package com.samuelji.fishgame.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

public class UserCaughtFishDTO {
    @Data
    @NoArgsConstructor
    public static class Response {
        private String type;
        private double weight;
        private String description;
        private String imageUrl;
        private String rank;
    }
}
