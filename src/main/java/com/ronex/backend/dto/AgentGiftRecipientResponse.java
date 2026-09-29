package com.ronex.backend.dto;

public record AgentGiftRecipientResponse(
        Long userId,
        String name,
        String phone
) {
}