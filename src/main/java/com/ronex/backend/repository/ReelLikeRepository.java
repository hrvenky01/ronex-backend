package com.ronex.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ronex.backend.model.ReelLike;

@Repository
public interface ReelLikeRepository extends JpaRepository<ReelLike, Long> {
}