package com.ronex.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ronex.backend.model.GiftRequest;
import com.ronex.backend.model.Ledger;
import com.ronex.backend.model.Notification;

import com.ronex.backend.repository.GiftRequestRepository;
import com.ronex.backend.repository.LedgerRepository;
import com.ronex.backend.repository.NotificationRepository;
import com.ronex.backend.wallet.WalletRepository;

@RestController
@RequestMapping("/api/admin/gifts")
@CrossOrigin("*")
public class AdminGiftController {

    @Autowired
    private GiftRequestRepository giftRepo;

    @Autowired
    private WalletRepository walletRepo;

    @Autowired
    private LedgerRepository ledgerRepo;

    @Autowired
    private NotificationRepository notificationRepo;

    // 1. Get all pending requests
    @GetMapping("/pending")
    public List<GiftRequest> getPending() {
        return giftRepo.findByStatus("PENDING");
    }

    // 2. Approve request (FULL FLOW)
    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {

        GiftRequest req = giftRepo.findById(id).orElse(null);

        if (req == null) return "Not found";

        // STEP 1: Update status
        req.setStatus("APPROVED");
        giftRepo.save(req);

        // STEP 2: Wallet credit
        walletRepo.findByUserId(req.getUserId()).ifPresent(wallet -> {
            wallet.setBalance(wallet.getBalance() + req.getAmount());
            walletRepo.save(wallet);
        });

        // STEP 3: Ledger entry
        Ledger ledger = new Ledger();
        ledger.setUserId(req.getUserId());
        ledger.setAmount(req.getAmount());
        ledger.setType("CREDIT");
        ledger.setReason("GIFT_APPROVED");
        ledgerRepo.save(ledger);

        // STEP 4: Notification
        Notification notification = new Notification();
        notification.setUserId(req.getUserId());
        notification.setMessage("🎁 Gift approved: +" + req.getAmount() + " coins");
        notification.setType("GIFT");

        notificationRepo.save(notification);

        return "Approved + Wallet Credited + Ledger Updated + Notification Sent";
    }

    // 3. Reject request
    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id) {

        GiftRequest req = giftRepo.findById(id).orElse(null);

        if (req == null) return "Not found";

        req.setStatus("REJECTED");
        giftRepo.save(req);

        // OPTIONAL: Notification for rejection
        Notification notification = new Notification();
        notification.setUserId(req.getUserId());
        notification.setMessage("❌ Gift request rejected");
        notification.setType("GIFT");

        notificationRepo.save(notification);

        return "Rejected + Notification Sent";
    }
}