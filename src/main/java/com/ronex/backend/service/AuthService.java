package com.ronex.backend.service;

public interface AuthService {

    // 📱 OTP (legacy / optional)
    String sendOtp(String phone);
    String verifyOtp(String phone, String otp);

    // 🔥 Firebase login support
    void ensureUserExists(String phone);

    // 🔥 ROLE SUPPORT (USER / AGENT)
    String getUserRole(String phone);

    // 🔥 REFERRAL
    void applyReferral(Long newUserId, String referralCode);
}