package com.ronex.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String message;

    private String type; // GIFT, WALLET, SYSTEM

    private boolean readStatus = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters & setters
}