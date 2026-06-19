package com.ronex.backend.util;

import java.util.UUID;

public class ReferralUtil {

    public static String generate() {
        return "RX" + UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();
    }
}