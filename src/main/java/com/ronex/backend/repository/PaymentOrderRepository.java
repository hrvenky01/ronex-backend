package com.ronex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ronex.backend.model.PaymentOrder;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    PaymentOrder findByOrderId(String orderId);
}