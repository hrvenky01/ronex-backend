package com.ronex.backend.dto;

import java.time.LocalDateTime;

public record ManagerGiftRequestResponse(
        Long id,

        Long agentUserId,
        String agentUsername,

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