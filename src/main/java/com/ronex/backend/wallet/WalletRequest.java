package com.ronex.backend.wallet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletRequest {
    private Long userId;
    private long amount;
    private String source; // GREEDY_WHEEL / 777 / ADMIN
}