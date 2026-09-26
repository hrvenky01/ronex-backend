package com.ronex.backend.repository;

import com.ronex.backend.model.GiftRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GiftRequestRepository
        extends JpaRepository<GiftRequest, Long> {

    // Existing code compatibility
    List<GiftRequest> findByStatus(String status);

    // Manager Gift Requests
    List<GiftRequest> findByStatusOrderByCreatedAtDesc(String status);

    // Manager / Admin statistics
    long countByStatus(String status);

    long countByStatusAndUpdatedAtGreaterThanEqualAndUpdatedAtLessThan(
            String status,
            LocalDateTime start,
            LocalDateTime end
    );

    // All requests for Manager
    List<GiftRequest> findAllByOrderByCreatedAtDesc();
}