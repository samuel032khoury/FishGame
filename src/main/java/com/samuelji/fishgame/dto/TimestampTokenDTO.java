package com.samuelji.fishgame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class TimestampTokenDTO {
    @Data
    @AllArgsConstructor
    public static class Response {
        private String token;
        private String timeStamp;
    }
}
