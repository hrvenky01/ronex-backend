package com.ronex.backend.dto;

public record CreateManagerRequest(
        String username,
        String password
) {
}