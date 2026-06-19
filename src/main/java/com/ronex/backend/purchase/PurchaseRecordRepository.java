package com.ronex.backend.purchase;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRecordRepository
        extends JpaRepository<PurchaseRecord, Long> {

    boolean existsByPurchaseToken(String purchaseToken);
}