package com.ronex.backend.wallet;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wallet_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private long amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type; // CREDIT / DEBIT

    private String source; // GREEDY_WHEEL / ADMIN / COLOR_GAME

    private long balanceAfter;

    private LocalDateTime createdAt = LocalDateTime.now();
    
    
    private String paymentId;
    private int coins;
    private String orderId;

    private String status; // SUCCESS / FAILED
}