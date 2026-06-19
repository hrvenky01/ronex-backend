package com.ronex.backend.repository;

import com.ronex.backend.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findByPhone(String phone);

    void deleteByPhone(String phone);
}