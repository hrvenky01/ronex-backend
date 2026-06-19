package com.ronex.backend.wallet;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final WalletTransactionRepository transactionRepository;
    @Autowired
    private WalletRepository walletRepo;

    // 🔹 Get wallet balance
    @GetMapping
    public Wallet getWallet(@RequestParam Long userId) {
        return walletService.getWallet(userId);
    }

    // 🔹 Get transaction history
    @GetMapping("/transactions")
    public List<WalletTransaction> transactions(@RequestParam Long userId) {
        return transactionRepository
                .findByUserIdOrderByCreatedAtDesc(userId);
    }

    // 🔹 CREDIT coins (admin / game win)
    @PostMapping("/credit")
    public void credit(@RequestBody WalletRequest req) {
        walletService.credit(
                req.getUserId(),
                req.getAmount(),
                req.getSource()
        );
    }
    
    @GetMapping("/api/admin/wallets")
    public List<Wallet> getWallets() {
        return walletRepo.findAll();
    }

    // 🔹 DEBIT coins (game bet)
    @PostMapping("/debit")
    public void debit(@RequestBody WalletRequest req) {
        walletService.debit(
                req.getUserId(),
                req.getAmount(),
                req.getSource()
        );
    }
}