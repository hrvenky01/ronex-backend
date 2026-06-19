// SpinRequest.java
package com.ronex.backend.dto;

public class GreedySpinRequest {
    private Long userId;
    private Long betAmount;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getBetAmount() { return betAmount; }
    public void setBetAmount(Long betAmount) { this.betAmount = betAmount; }
}