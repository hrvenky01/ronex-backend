package com.ronex.backend.game777;

public enum SevenResultType {
    NO_MATCH(0),
    DOUBLE_MATCH(2),
    TRIPLE_MATCH(5),
    JACKPOT(20);

    private final int multiplier;

    SevenResultType(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }
}