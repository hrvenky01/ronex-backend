package com.ronex.backend.repository;

import com.ronex.backend.model.ReferralHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferralHistoryRepository extends JpaRepository<ReferralHistory, Long> {

    List<ReferralHistory> findByReferrerId(Long referrerId);
}