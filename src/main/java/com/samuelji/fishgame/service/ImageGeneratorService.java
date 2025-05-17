package com.samuelji.fishgame.service;

import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuelji.fishgame.config.OpenAIConfig;
import com.samuelji.fishgame.model.Image;
import com.samuelji.fishgame.repository.ImageRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ImageGeneratorService {
    private final RestTemplate restTemplate;
    private final ImageRepository imageRepository;
    private final ObjectMapper objectMapper;
    private final OpenAIConfig openAIConfig;

    @Async("taskExecutor")
    public void regenerateImages(int count) {
        String url = openAIConfig.getImageGenUrl();
        for (int i = 0; i < count; i++) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(openAIConfig.getKey());
            headers.setContentType(MediaType.APPLICATION_JSON);

            JsonNode body = objectMapper.createObjectNode()
                    .put("prompt", "A beautiful fish in the ocean")
                    .put("n", 1)
                    .put("size", "1024x1024");

            try {
                ResponseEntity<JsonNode> resp = restTemplate.postForEntity(url, new HttpEntity<>(body, headers),
                        JsonNode.class);
                if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                    JsonNode data = resp.getBody();
                    if (data != null) {
                        String imageUrl = data.path("data").get(0).path("url").asText();
                        imageRepository.save(new Image("AutoGen_" + System.currentTimeMillis(), imageUrl));
                    } else {
                        throw new RuntimeException("No data in response");
                    }
                }
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                log.error("Image generation failed", e);
            }
        }
    }
}
