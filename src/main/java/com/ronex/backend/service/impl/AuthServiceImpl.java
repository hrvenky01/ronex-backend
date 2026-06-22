package com.ronex.backend.service.impl;

import com.ronex.backend.model.*;
import com.ronex.backend.repository.*;
import com.ronex.backend.service.AuthService;
import com.ronex.backend.util.ReferralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReferralCodeRepository referralCodeRepository;

    @Autowired
    private ReferralHistoryRepository referralHistoryRepository;

    // =========================
    // 🔥 SEND OTP (optional)
    // =========================
    @Override
    public String sendOtp(String phone) {

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        otpRepository.deleteByPhone(phone);

        Otp o = new Otp();
        o.setPhone(phone);
        o.setOtp(otp);
        o.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(o);

        System.out.println("🔥 OTP for " + phone + " = " + otp);
        return "OTP sent successfully";
    }

    // =========================
    // 🔥 VERIFY OTP (optional)
    // =========================
    @Override
    public String verifyOtp(String phone, String otp) {

        Otp o = otpRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (o.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!o.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        ensureUserExists(phone);
        otpRepository.deleteByPhone(phone);

        return "OTP verified successfully";
    }

    // =========================
    // 🔥 FIREBASE FLOW CORE
    // =========================
    @Override
    public void ensureUserExists(String phone) {

        User user = userRepository.findByPhone(phone)
                .orElseGet(() -> {
                    User u = new User();
                    u.setPhone(phone);
                    u.setRole("USER");
                    u.setBalance(0L);
                    u.setWalletBalance(0L);
                    return userRepository.save(u);
                });

        // 🔥 auto referral code create
        referralCodeRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    ReferralCode rc = new ReferralCode();
                    rc.setUserId(user.getId());
                    rc.setCode(ReferralUtil.generate());
                    return referralCodeRepository.save(rc);
                });
    }

    // =========================
    // 🔥 APPLY REFERRAL
    // =========================
    @Override
    public void applyReferral(Long newUserId, String referralCode) {

        ReferralCode code = referralCodeRepository.findByCode(referralCode)
                .orElseThrow(() -> new RuntimeException("Invalid referral code"));

        User referrer = userRepository.findById(code.getUserId())
                .orElseThrow(() -> new RuntimeException("Referrer not found"));

        User newUser = userRepository.findById(newUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (referrer.getId().equals(newUser.getId())) {
            throw new RuntimeException("Self referral not allowed");
        }

        newUser.setReferredBy(referrer.getId());

        long reward = "AGENT".equals(referrer.getRole()) ? 300 : 200;
        referrer.setWalletBalance(referrer.getWalletBalance() + reward);

        saveHistory(referrer.getId(), newUserId, reward,
                "AGENT".equals(referrer.getRole()) ? "AGENT_COMMISSION" : "USER_REFERRAL");

        userRepository.save(referrer);
        userRepository.save(newUser);
    }

    private void saveHistory(Long referrerId, Long newUserId, long amount, String type) {
        ReferralHistory h = new ReferralHistory();
        h.setReferrerId(referrerId);
        h.setReferredUserId(newUserId);
        h.setAmount(amount);
        h.setType(type);
        h.setCreatedAt(LocalDateTime.now());
        referralHistoryRepository.save(h);
    }

    // =========================
    // 🔥 ROLE FETCH
    // =========================
    @Override
    public String getUserRole(String phone) {
        return userRepository.findByPhone(phone)
                .map(User::getRole)
                .orElse("USER");
    }
}