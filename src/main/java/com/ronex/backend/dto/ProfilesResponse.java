// ProfileResponse.java
package com.ronex.backend.dto;

public record ProfilesResponse(
        String username,
        String avatarUrl,
        int followers,
        int following
) {}