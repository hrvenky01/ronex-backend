package com.ronex.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ronex.backend.model.ReferralHistory;
import com.ronex.backend.model.User;
import com.ronex.backend.repository.ReferralHistoryRepository;
import com.ronex.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/referrals")
@RequiredArgsConstructor
public class AdminReferralController {

    private final ReferralHistoryRepository historyRepo;
    private final UserRepository userRepo;

    @GetMapping("/all")
    public List<ReferralHistory> allReferrals() {
        return historyRepo.findAll();
    }

    @PostMapping("/make-agent/{userId}")
    public String makeAgent(@PathVariable Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        user.setRole("AGENT");
        user.setAgentSince(LocalDateTime.now());
        userRepo.save(user);
        return "User promoted to AGENT";
    }
}
