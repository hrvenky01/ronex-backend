package com.ronex.backend.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.ronex.backend.dto.AuthResponse;
import com.ronex.backend.model.Otp;
import com.ronex.backend.model.User;
import com.ronex.backend.repository.OtpRepository;
import com.ronex.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;

    // 🔥 SEND OTP
    public void sendOtp(String phone) {

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        otpRepository.deleteByPhone(phone);

        Otp entity = new Otp();
        entity.setPhone(phone);
        entity.setOtp(otp);
        entity.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(entity);

        System.out.println("🔥 OTP for " + phone + " = " + otp);
    }

    // 🔥 VERIFY OTP
    public AuthResponse verifyOtp(String phone, String otp) {

        Otp savedOtp = otpRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (savedOtp.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");

        if (!savedOtp.getOtp().equals(otp))
            throw new RuntimeException("Invalid OTP");

        // USER CREATE / FIND
        User user = userRepository.findByPhone(phone)
                .orElseGet(() -> {
                    User u = new User();
                    u.setPhone(phone);
                    u.setRole("ROLE_USER");
                    u.setBalance(0L);
                    u.setWalletBalance(0L);
                    return userRepository.save(u);
                });

        otpRepository.deleteByPhone(phone);

        String token = "jwt_" + user.getId() + "_" + System.currentTimeMillis();

        return new AuthResponse(
                token,
                user.getId(),
                user.getRole()
        );
    }
}