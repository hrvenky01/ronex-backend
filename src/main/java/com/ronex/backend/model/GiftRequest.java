package com.ronex.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "gift_requests")
public class GiftRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * RONEX User ID of the Agent who created the request.
     */
    @Column(name = "agent_user_id")
    private Long agentUserId;

    /**
     * RONEX User ID of the person who should receive the coins.
     */
    @Column(name = "recipient_user_id")
    private Long recipientUserId;

    /**
     * OLD FIELD - kept temporarily for existing database compatibility.
     *
     * New requests should use recipientUserId.
     */
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private Integer amount;

    @Column(length = 1000)
    private String reason;

    @Column(length = 2000)
    private String url;

    /**
     * PENDING
     * APPROVED
     * REJECTED
     */
    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null || this.status.isBlank()) {
            this.status = "PENDING";
        }
    }

    @PreUpdate
    protected void onUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}