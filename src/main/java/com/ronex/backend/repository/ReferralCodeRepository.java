package com.ronex.backend.repository;

import com.ronex.backend.model.ReferralCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReferralCodeRepository extends JpaRepository<ReferralCode, Long> {

    Optional<ReferralCode> findByCode(String code);
    Optional<ReferralCode> findByUserId(Long userId);
}