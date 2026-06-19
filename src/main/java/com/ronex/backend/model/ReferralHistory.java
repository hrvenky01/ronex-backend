package com.ronex.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "referral_history")
public class ReferralHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long referrerId;
    private Long referredUserId;

    private long amount;

    private String type; // USER_REFERRAL / AGENT_COMMISSION

    private LocalDateTime createdAt = LocalDateTime.now();
}