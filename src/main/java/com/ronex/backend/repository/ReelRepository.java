package com.ronex.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ronex.backend.model.Reel;

public interface ReelRepository extends JpaRepository<Reel, Long> {
    List<Reel> findAllByOrderByCreatedAtDesc();
}