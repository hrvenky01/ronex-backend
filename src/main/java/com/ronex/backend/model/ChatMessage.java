package com.ronex.backend.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private String roomId;
    private String sender;
    private String message;
    private String type; // CHAT / JOIN / LEAVE
    private LocalDateTime time = LocalDateTime.now();
}