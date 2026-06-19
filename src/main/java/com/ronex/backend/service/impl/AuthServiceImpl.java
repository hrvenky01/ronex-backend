package com.ronex.backend.service.impl;

import com.ronex.backend.model.Otp;
import com.ronex.backend.model.User;
import com.ronex.backend.model.ReferralCode;
import com.ronex.backend.model.ReferralHistory;
import com.ronex.backend.repository.OtpRepository;
import com.ronex.backend.repository.UserRepository;
import com.ronex.backend.repository.ReferralCodeRepository;
import com.ronex.backend.repository.ReferralHistoryRepository;
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

    // 🔥 NEW
    @Autowired
    private ReferralCodeRepository referralCodeRepository;

    @Autowired
    private ReferralHistoryRepository referralHistoryRepository;

    // =========================
    // 🔥 SEND OTP
    // =========================
    @Override
    public String sendOtp(String phone) {

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        otpRepository.deleteByPhone(phone);

        Otp otpEntity = new Otp();
        otpEntity.setPhone(phone);
        otpEntity.setOtp(otp);
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(otpEntity);

        System.out.println("🔥 OTP for " + phone + " = " + otp);

        return "OTP sent successfully";
    }

    // =========================
    // 🔥 VERIFY OTP
    // =========================
    @Override
    public String verifyOtp(String phone, String otp) {

        Otp otpEntity = otpRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!otpEntity.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // =========================
        // 👤 USER CREATE / FIND
        // =========================
        User user = userRepository.findByPhone(phone)
                .orElseGet(() -> {
                    User u = new User();
                    u.setPhone(phone);
                    u.setRole("USER");        // clean role
                    u.setBalance(0L);
                    u.setWalletBalance(0L);
                    return userRepository.save(u);
                });

        // =========================
        // 🔥 REFERRAL CODE AUTO CREATE
        // =========================
        referralCodeRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    ReferralCode rc = new ReferralCode();
                    rc.setUserId(user.getId());
                    rc.setCode(ReferralUtil.generate());
                    return referralCodeRepository.save(rc);
                });

        otpRepository.deleteByPhone(phone);

        return "OTP verified successfully";
    }

    // =========================
    // 🔥 APPLY REFERRAL CODE
    // =========================
    @Override
    public void applyReferral(Long newUserId, String referralCode) {

        ReferralCode code = referralCodeRepository.findByCode(referralCode)
                .orElseThrow(() -> new RuntimeException("Invalid referral code"));

        User referrer = userRepository.findById(code.getUserId())
                .orElseThrow();

        User newUser = userRepository.findById(newUserId)
                .orElseThrow();

        // ❌ self referral block
        if (referrer.getId().equals(newUser.getId())) {
            throw new RuntimeException("Self referral not allowed");
        }

        newUser.setReferredBy(referrer.getId());

        long reward;

        // 🔥 AGENT vs USER reward
        if ("AGENT".equals(referrer.getRole())) {
            reward = 300;
            referrer.setWalletBalance(referrer.getWalletBalance() + reward);
            saveHistory(referrer.getId(), newUserId, reward, "AGENT_COMMISSION");
        } else {
            reward = 200;
            referrer.setWalletBalance(referrer.getWalletBalance() + reward);
            saveHistory(referrer.getId(), newUserId, reward, "USER_REFERRAL");
        }

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