package com.samuelji.fishgame.dto;

import com.samuelji.fishgame.model.Fish;

import lombok.AllArgsConstructor;
import lombok.Data;

public class CaughtFishDTO {
    @Data
    @AllArgsConstructor
    public static class Response {
        private String fish;
        private double weight;
        private String description;
        private String imageUrl;
        private String status;

        public static Response build(Fish fish) {
            return new Response(fish.getType(), fish.getWeight(), fish.getDescription(), fish.getUrl(), "success");
        }
    }
}
