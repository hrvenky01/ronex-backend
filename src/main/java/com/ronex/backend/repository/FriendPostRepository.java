package com.ronex.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ronex.backend.model.FriendPost;

public interface FriendPostRepository extends JpaRepository<FriendPost, Long> {
    List<FriendPost> findAllByOrderByCreatedAtDesc();
}