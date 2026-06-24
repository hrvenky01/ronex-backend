package com.ronex.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ReelComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reelId;

    private String userName;   // ✅ ADD THIS

    private String comment;

    @Column(updatable = false)
    private Long createdAt = System.currentTimeMillis();
}