package com.ronex.backend.purchase;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "purchase_records",
    uniqueConstraints = @UniqueConstraint(columnNames = "purchaseToken")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String productId;

    @Column(nullable = false, unique = true)
    private String purchaseToken;

    private long coins;

    private LocalDateTime purchasedAt = LocalDateTime.now();
}