package com.samuelji.fishgame.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samuelji.fishgame.service.ChatGenService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {
    private final ChatGenService chatGenService;

    @GetMapping
    public String chatRoom() {
        return "Welcome to the Chat Room!";
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateChatResponse(@RequestBody String prompt) {
        return ResponseEntity.ok(chatGenService.generateChatResponse(prompt));
    }

    @PostMapping("/help")
    public ResponseEntity<String> getHelp(@RequestBody String prompt) {
        String helpPrompt = "You are a helpful assistant for a game. Now the user wants to take an action but is unsure how to proceed. The available options are 'go to shop (for tool upgrades)', 'catch a fish', 'sell fish', 'check user profile', 'check fish inventory'. Please provide one of the options based on the user intent: "
                + prompt
                + ".\n\nRespond with only one of the options: 'go to shop', 'catch a fish', 'sell fish', 'check profile', 'check inventory'. Do not provide any additional information or explanations."
                + "if none of the options are applicable, respond with 'I don't know'.";
        return ResponseEntity.ok(chatGenService.generateChatResponse(helpPrompt));
    }

}