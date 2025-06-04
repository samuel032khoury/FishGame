package com.samuelji.fishgame.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "openai.api")
public class OpenAIConfig {
    private String key;
    private String imageGenUrl;
}
