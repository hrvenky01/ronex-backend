package com.ronex.backend.service;

import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ronex.backend.dto.GreedySpinResponse;
import com.ronex.backend.wallet.Wallet;
import com.ronex.backend.wallet.WalletRepository;

@Service
public class GreedyWheelService {

    private final WalletRepository walletRepo;
    private final BetValidationService betValidationService;
    private final Random random = new Random();

    // Frontend order must match
    private static final String[] LABELS = {
        "APPLE", "DOG", "BANANA", "CAT",
        "ORANGE", "TIGER", "WATERMELON", "LION"
    };

    private static final int[] MULTIPLIERS = {
        2, 7, 2, 15, 3, 25, 5, 35
    };

    // 🎯 House Edge weights
    private static final int[] WEIGHTS = {
        17, // APPLE
        10, // DOG
        16, // BANANA
        7,  // CAT
        14, // ORANGE
        3,  // TIGER
        12, // WATERMELON
        2   // LION
    };

    public GreedyWheelService(
            WalletRepository walletRepo,
            BetValidationService betValidationService) {
        this.walletRepo = walletRepo;
        this.betValidationService = betValidationService;
    }

    @Transactional
    public GreedySpinResponse spin(Long userId, long bet) {

        // 🔒 FINAL AUTHORITY
        betValidationService.validateBet(bet);

        Wallet wallet = walletRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance() < bet) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance() - bet);

        int index = pickWeightedIndex();
        int multiplier = MULTIPLIERS[index];
        long winAmount = bet * multiplier;

        wallet.setBalance(wallet.getBalance() + winAmount);
        walletRepo.save(wallet);

        return new GreedySpinResponse(
                index,
                LABELS[index],
                multiplier,
                winAmount,
                wallet.getBalance()
        );
    }
    private int pickWeightedIndex() {
        int total = 0;
        for (int w : WEIGHTS) total += w;

        int r = random.nextInt(total);
        int upto = 0;

        for (int i = 0; i < WEIGHTS.length; i++) {
            upto += WEIGHTS[i];
            if (r < upto) return i;
        }
        return 0;
    }
}