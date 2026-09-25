package com.ronex.backend.dto;

public record ManagerDashboardStatsResponse(
        long pendingGifts,
        long approvedToday,
        long rejectedToday,
        long totalAgents
) {
}