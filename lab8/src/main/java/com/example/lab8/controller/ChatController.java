package com.example.lab8.controller;

import com.example.lab8.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat") // Client gửi đến /app/chat
    @SendTo("/topic/messages") // Server gửi đến tất cả client subscribe /topic/messages
    public ChatMessage send(ChatMessage message) {
        return message;
    }
}
