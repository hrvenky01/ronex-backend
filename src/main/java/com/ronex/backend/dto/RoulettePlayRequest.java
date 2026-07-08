package com.ronex.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoulettePlayRequest {
    private int betNumber;
    private long betAmount;
}