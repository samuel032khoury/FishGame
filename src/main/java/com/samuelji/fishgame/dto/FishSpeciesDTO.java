package com.samuelji.fishgame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

public class FishSpeciesDTO {
    @Data
    public static class Request {
        private String type;
        private String description;
        private Double probability;
        @JsonProperty("sWeight")
        private Double sWeight;
        @JsonProperty("aWeight")
        private Double aWeight;
        @JsonProperty("bWeight")
        private Double bWeight;
        @JsonProperty("cWeight")
        private Double cWeight;
        private Double mean;
        private Double standardDeviation = 0.625;
        private Boolean status = true;
    }

    @Data
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String type;
        private String description;
        private Double probability;
    }

    @Data
    @NoArgsConstructor
    public static class VerboseResponse {
        private String type;
        private String description;
        private Double probability;
        @JsonProperty("sWeight")
        private Double sWeight;
        @JsonProperty("aWeight")
        private Double aWeight;
        @JsonProperty("bWeight")
        private Double bWeight;
        @JsonProperty("cWeight")
        private Double cWeight;
        private Double mean;
        private Double standardDeviation;
        private Boolean status;
    }

}
