package com.samuelji.fishgame.config;

import java.util.concurrent.Executor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.Data;

@EnableAsync
@Data
@Configuration
@ConfigurationProperties(prefix = "async.thread")
public class ImageGenConfig {
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;

    @Bean("ImageGenExecutor")
    public Executor ImageGenExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("ImageGen-");
        executor.initialize();
        return executor;
    }

}
