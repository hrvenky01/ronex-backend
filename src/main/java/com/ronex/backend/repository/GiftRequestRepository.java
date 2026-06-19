package com.ronex.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ronex.backend.model.GiftRequest;

public interface GiftRequestRepository extends JpaRepository<GiftRequest, Long> {
    List<GiftRequest> findByStatus(String status);
    long countByStatus(String status);
}