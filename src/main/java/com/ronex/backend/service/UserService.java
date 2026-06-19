package com.ronex.backend.service;

import com.ronex.backend.dto.ProfileResponse;
import com.ronex.backend.model.User;
import com.ronex.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // =========================
    // PROFILE API
    // =========================
    public ProfileResponse getProfile(String phone) {

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getWalletBalance(),
                1200L,   // TEMP followers
                350L     // TEMP following
        );
    }

    // =========================
    // 🔥 USER → AGENT UPGRADE
    // =========================
    @Transactional
    public String upgradeToAgent(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // already agent check
        if ("AGENT".equalsIgnoreCase(user.getRole())) {
            return "User is already an AGENT";
        }

        // admin protection
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return "ADMIN cannot be upgraded";
        }

        // upgrade user → agent
        user.setRole("AGENT");

        userRepository.save(user);

        return "User successfully upgraded to AGENT";
    }
}