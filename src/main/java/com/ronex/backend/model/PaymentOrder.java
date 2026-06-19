package com.ronex.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class PaymentOrder {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;
    private int coins;
    private int amount;

    private String orderId;
    private String status; // CREATED / PAID
}