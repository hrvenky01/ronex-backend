package com.ronex.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {
    private Long id;
    private String name;
    private String phone;
    private Long walletBalance;
    private Long followers;
    private Long following;
    
}