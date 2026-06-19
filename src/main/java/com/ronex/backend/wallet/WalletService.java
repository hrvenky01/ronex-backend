package com.ronex.backend.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;

    // 🔹 Get or create wallet
    public Wallet getWallet(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Wallet w = new Wallet();
                    w.setUserId(userId);
                    w.setBalance(0);
                    return walletRepository.save(w);
                });
    }

    // 🔹 CREDIT coins (Concurrency Safe)
    @Transactional
    public void credit(Long userId, long amount, String source) {
        try {
            if (amount <= 0)
                throw new RuntimeException("Invalid credit amount");

            Wallet wallet = getWallet(userId);

            if (wallet.isLocked())
                throw new RuntimeException("Wallet is locked");

            long newBalance = wallet.getBalance() + amount;
            wallet.setBalance(newBalance);
            walletRepository.save(wallet);

            WalletTransaction tx = new WalletTransaction();
            tx.setUserId(userId);
            tx.setAmount(amount);
            tx.setType(TransactionType.CREDIT);
            tx.setSource(source);
            tx.setBalanceAfter(newBalance);

            transactionRepository.save(tx);

        } catch (OptimisticLockException e) {
            throw new RuntimeException("Wallet busy. Please retry.");
        }
    }

    // 🔹 DEBIT coins (Concurrency Safe)
    @Transactional
    public void debit(Long userId, long amount, String source) {
        try {
            if (amount <= 0)
                throw new RuntimeException("Invalid debit amount");

            Wallet wallet = getWallet(userId);

            if (wallet.isLocked())
                throw new RuntimeException("Wallet is locked");

            if (wallet.getBalance() < amount)
                throw new RuntimeException("Insufficient balance");

            long newBalance = wallet.getBalance() - amount;
            wallet.setBalance(newBalance);
            walletRepository.save(wallet);

            WalletTransaction tx = new WalletTransaction();
            tx.setUserId(userId);
            tx.setAmount(amount);
            tx.setType(TransactionType.DEBIT);
            tx.setSource(source);
            tx.setBalanceAfter(newBalance);

            transactionRepository.save(tx);

        } catch (OptimisticLockException e) {
            throw new RuntimeException("Wallet busy. Please retry.");
        }
    }
}