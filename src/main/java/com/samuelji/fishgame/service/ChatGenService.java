package com.samuelji.fishgame.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuelji.fishgame.config.OpenAIConfig;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ChatGenService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final OpenAIConfig openAIConfig;

    public String generateChatResponse(String prompt) {
        String url = openAIConfig.getChatGenUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAIConfig.getKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        JsonNode body = objectMapper.createObjectNode()
                .put("model", "gpt-3.5-turbo-instruct")
                .put("prompt", prompt)
                .put("max_tokens", 100)
                .put("temperature", 0.7);
        try {
            ResponseEntity<JsonNode> resp = restTemplate.postForEntity(
                    url,
                    new HttpEntity<>(body, headers),
                    JsonNode.class);

            JsonNode responseBody = resp.getBody();
            if (resp.getStatusCode().is2xxSuccessful() && responseBody != null) {
                JsonNode choices = responseBody.path("choices");
                if (choices.isArray() && choices.size() > 0) {
                    String generatedText = choices.get(0).path("text").asText();
                    log.info("Generated text: {}", generatedText);
                    return generatedText;
                } else {
                    throw new RuntimeException("No choices in response");
                }
            } else {
                throw new RuntimeException("Failed to get a valid response");
            }
        } catch (Exception e) {
            log.error("Chat generation failed", e);
            return "Error generating response";
        }
    }
}
