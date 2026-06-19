package com.ronex.backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.ronex.backend.model.ChatMessage;

@Controller
public class ChatController {

    @MessageMapping("/chat.send")
    @SendTo("/topic/room")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }

    @MessageMapping("/chat.join")
    @SendTo("/topic/room")
    public ChatMessage join(ChatMessage message) {
        message.setType("JOIN");
        return message;
    }
}