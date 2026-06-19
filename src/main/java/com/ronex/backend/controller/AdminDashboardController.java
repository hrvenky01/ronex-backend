package com.ronex.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ronex.backend.dto.UserGrowthDto;
import com.ronex.backend.model.Ledger;
import com.ronex.backend.repository.GiftRequestRepository;
import com.ronex.backend.repository.LedgerRepository;
import com.ronex.backend.repository.UserRepository;
import com.ronex.backend.wallet.WalletRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin("*")
public class AdminDashboardController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GiftRequestRepository giftRepo;

    @Autowired
    private WalletRepository walletRepo;

    @Autowired
    private LedgerRepository ledgerRepo;

    // ======================
    // 📊 DASHBOARD STATS
    // ======================
    @GetMapping("/stats")
    public Map<String, Object> getStats() {

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userRepo.count());
        stats.put("pendingGifts", giftRepo.countByStatus("PENDING"));
        stats.put("approvedGifts", giftRepo.countByStatus("APPROVED"));

        long totalCoins = walletRepo.findAll()
                .stream()
                .mapToLong(w -> w.getBalance())
                .sum();

        stats.put("totalCoins", totalCoins);

        return stats;
    }

    // ======================
    // 👥 USER GROWTH (DAILY)
    // ======================
    @GetMapping("/users-growth")
    public List<UserGrowthDto> getUserGrowth() {
        return userRepo.getUserGrowth();
    }

    // ======================
    // 📒 USER LEDGER
    // ======================
    @GetMapping("/ledger/{userId}")
    public List<Ledger> getLedger(@PathVariable Long userId) {
        return ledgerRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // ======================
    // 💰 DAILY REVENUE
    // ======================
    @GetMapping("/revenue")
    public List<Map<String, Object>> getRevenue() {

        List<Object[]> data = ledgerRepo.getDailyRevenue();

        List<Map<String, Object>> result = new java.util.ArrayList<>();

        for (Object[] row : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0]);
            map.put("revenue", row[1]);
            result.add(map);
        }

        return result;
    }
}