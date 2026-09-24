package com.ronex.backend.dto;

import java.time.LocalDateTime;

public record ManagerResponse(
        Long id,
        String username,
        String role,
        boolean active,
        LocalDateTime createdAt
) {
}