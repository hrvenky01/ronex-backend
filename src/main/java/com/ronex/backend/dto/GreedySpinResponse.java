// SpinResponse.java
package com.ronex.backend.dto;

public class GreedySpinResponse {
    private int resultIndex;
    private String label;
    private int multiplier;
    private long winAmount;
    private long walletBalance;

    public GreedySpinResponse(int resultIndex, String label, int multiplier, long winAmount, long walletBalance) {
        this.resultIndex = resultIndex;
        this.label = label;
        this.multiplier = multiplier;
        this.winAmount = winAmount;
        this.walletBalance = walletBalance;
    }

    public int getResultIndex() { return resultIndex; }
    public String getLabel() { return label; }
    public int getMultiplier() { return multiplier; }
    public long getWinAmount() { return winAmount; }
    public long getWalletBalance() { return walletBalance; }
}