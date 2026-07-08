package com.ronex.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoulettePlayResponse {
    private int resultNumber;
    private long winAmount;
    private long balance;
}