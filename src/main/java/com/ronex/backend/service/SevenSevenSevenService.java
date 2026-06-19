package com.ronex.backend.service;

import org.springframework.stereotype.Service;

import com.ronex.backend.dto.SevenSpinResponse;
import com.ronex.backend.game777.SevenSpinEngine;
import com.ronex.backend.game777.SevenResultType;
import com.ronex.backend.wallet.Wallet;
import com.ronex.backend.wallet.WalletService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SevenSevenSevenService {

    private final WalletService walletService;

    public SevenSpinResponse spin(Long userId, long bet) {

        // 🔒 Bet validation
        if (bet < 20 || bet > 10000)
            throw new RuntimeException("Invalid bet amount");

        Wallet wallet = walletService.getWallet(userId);

        // 🔻 Debit bet first
        walletService.debit(userId, bet, "777_GAME");

        // 🎰 Spin
        var spin = SevenSpinEngine.spin();

        SevenResultType type = spin.type;
        long winAmount = bet * type.getMultiplier();

        // 💰 Credit if win
        if (winAmount > 0) {
            walletService.credit(userId, winAmount, "777_GAME");
        }

        Wallet updated = walletService.getWallet(userId);

        return new SevenSpinResponse(
                spin.symbols,
                type.getMultiplier(),
                winAmount,
                updated.getBalance(),
                type.name()
        );
    }
}