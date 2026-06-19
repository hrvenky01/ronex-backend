package com.ronex.backend.wallet;

public class WalletConcurrencyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public WalletConcurrencyException(String message) {
        super(message);
    }
}