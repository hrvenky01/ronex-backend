package com.ronex.backend.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

import com.ronex.backend.dto.RoulettePlayRequest;
import com.ronex.backend.dto.RoulettePlayResponse;
import com.ronex.backend.wallet.WalletService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouletteService {

    private final WalletService walletService;

    private static final int[] NUMBERS =
            {1,2,3,4,5,6,7,8,9,10,11,12};

    private static final double PAYOUT = 2.5; // house edge applied

    @Transactional
    public RoulettePlayResponse play(
            Long userId,
            RoulettePlayRequest req
    ) {

        long betAmount = req.getBetAmount();
        int betNumber = req.getBetNumber();

        // 1️⃣ Debit bet
        walletService.debit(
                userId,
                betAmount,
                "ROULETTE"
        );

        // 2️⃣ Spin roulette
        int result =
                NUMBERS[new SecureRandom().nextInt(NUMBERS.length)];

        long winAmount = 0;

        // 3️⃣ Win check
        if (result == betNumber) {
            winAmount = (long) (betAmount * PAYOUT);

            walletService.credit(
                    userId,
                    winAmount,
                    "ROULETTE"
            );
        }

        // 4️⃣ Response
        RoulettePlayResponse res = new RoulettePlayResponse();
        res.setResultNumber(result);
        res.setWinAmount(winAmount);
        res.setBalance(
                walletService.getWallet(userId).getBalance()
        );

        return res;
    }
}