package com.ronex.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronex.backend.model.GiftRequest;
import com.ronex.backend.model.Ledger;
import com.ronex.backend.repository.GiftRequestRepository;
import com.ronex.backend.repository.LedgerRepository;
import com.ronex.backend.wallet.Wallet;
import com.ronex.backend.wallet.WalletRepository;

@Service
public class AdminGiftService {
	
	@Autowired
	private LedgerRepository ledgerRepo;

    @Autowired
    private GiftRequestRepository giftRepo;

    @Autowired
    private WalletRepository walletRepo;

    public String approveGift(Long id) {

        GiftRequest req = giftRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Gift not found"));

        if (!req.getStatus().equals("PENDING")) {
            return "Already processed";
        }

        // 1. Approve request
        req.setStatus("APPROVED");
        giftRepo.save(req);

        // 2. Find wallet
        Wallet wallet = walletRepo.findByUserId(req.getUserId())
                .orElse(null);

        // 3. If wallet not exist, create new
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUserId(req.getUserId());
            wallet.setBalance(0);
        }
        
        
        Ledger ledger = new Ledger();
        ledger.setUserId(req.getUserId());
        ledger.setAmount(req.getAmount());
        ledger.setType("CREDIT");
        ledger.setReason("GIFT_APPROVED");

        ledgerRepo.save(ledger);
        

        // 4. Add coins
        wallet.setBalance(wallet.getBalance() + req.getAmount());

        // 5. Save wallet
        walletRepo.save(wallet);

        return "Approved & Wallet Credited";
    }
}
