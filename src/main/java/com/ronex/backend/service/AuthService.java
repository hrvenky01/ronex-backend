package com.ronex.backend.service;

public interface AuthService {

    String sendOtp(String phone);

    String verifyOtp(String phone, String otp);

    // 🔥 ROLE SUPPORT (USER / AGENT)
    String getUserRole(String phone);
    
    // 🔥 NEW
    void applyReferral(Long newUserId, String referralCode);
}