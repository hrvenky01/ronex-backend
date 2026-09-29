package com.ronex.backend.dto;

import java.time.LocalDateTime;

public record AgentGiftRequestResponse(
        Long id,
        Long agentUserId,
        Long recipientUserId,
        String recipientName,
        String recipientPhone,
        Integer amount,
        String reason,
        String url,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}