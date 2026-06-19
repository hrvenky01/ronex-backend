package com.ronex.backend.wallet;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long userId;

    private long balance;

    private boolean locked = false;

    // 🔐 STEP-2: Optimistic Lock
    @Version
    private Long version;
}