package com.ronex.backend.dto;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String phone;
    private String otp;
}