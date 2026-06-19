package com.ronex.backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.ronex.backend.model.ChatMessage;

@Controller
public class ChatSocketController {

    @MessageMapping("/private.chat")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage msg) {
        return msg;
    }

    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public ChatMessage typing(ChatMessage msg) {
        msg.setType("TYPING");
        return msg;
    }

    @MessageMapping("/seen")
    @SendTo("/topic/seen")
    public ChatMessage seen(ChatMessage msg) {
        msg.setType("SEEN");
        return msg;
    }
}