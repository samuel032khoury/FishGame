package com.samuelji.fishgame.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @GetMapping
    public String chatRoom() {
        return "Welcome to the Chat Room!";
    }
}