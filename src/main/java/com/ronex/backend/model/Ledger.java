package com.ronex.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Integer amount;

    private String type; 
    // CREDIT or DEBIT

    private String reason;
    // GIFT, GAME, REFERRAL, ADMIN

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters & setters
}