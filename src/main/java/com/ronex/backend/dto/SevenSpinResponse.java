package com.ronex.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SevenSpinResponse {

    private List<String> symbols;
    private int multiplier;
    private long winAmount;
    private long balance;
    private String result;
}