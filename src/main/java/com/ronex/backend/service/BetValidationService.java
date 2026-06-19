package com.ronex.backend.service;

import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class BetValidationService {

    // ✅ Same as frontend BET_AMOUNTS
    private static final Set<Long> ALLOWED_BASE_BETS = Set.of(
        20L, 50L, 100L, 500L, 1000L, 2000L, 5000L
    );

    private static final long MIN_BET = 20;
    private static final long MAX_BET = 100_000;

    public void validateBet(long totalBet) {

        if (totalBet < MIN_BET) {
            throw new RuntimeException("Minimum bet is " + MIN_BET);
        }

        if (totalBet > MAX_BET) {
            throw new RuntimeException("Maximum bet exceeded");
        }

        boolean valid = ALLOWED_BASE_BETS
                .stream()
                .anyMatch(base -> totalBet % base == 0);

        if (!valid) {
            throw new RuntimeException(
                "Invalid bet amount: " + totalBet
            );
        }
    }
}