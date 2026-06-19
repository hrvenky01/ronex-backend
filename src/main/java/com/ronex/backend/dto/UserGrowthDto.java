package com.ronex.backend.dto;

import java.time.LocalDate;

public class UserGrowthDto {

    private LocalDate date;
    private Long count;

    // ⭐ REQUIRED constructor for DTO projection
    public UserGrowthDto(LocalDate date, Long count) {
        this.date = date;
        this.count = count;
    }

    // getters
    public LocalDate getDate() {
        return date;
    }

    public Long getCount() {
        return count;
    }
}