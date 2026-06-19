package com.ronex.backend.purchase;

import lombok.Data;

@Data
public class PurchaseRequest {
    private Long userId;
    private String productId;      // play store product id
    private String purchaseToken;  // google token
    private long coins;            // pack coins
}