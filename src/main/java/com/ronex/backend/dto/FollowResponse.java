// FollowResponse.java
package com.ronex.backend.dto;

public record FollowResponse(
        boolean following,
        int followers
) {}