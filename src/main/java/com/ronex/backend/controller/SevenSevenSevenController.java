package com.ronex.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.ronex.backend.dto.SevenSpinRequest;
import com.ronex.backend.dto.SevenSpinResponse;
import com.ronex.backend.service.SevenSevenSevenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/777")
@RequiredArgsConstructor
public class SevenSevenSevenController {

    private final SevenSevenSevenService service;

    @PostMapping("/spin")
    public SevenSpinResponse spin(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody SevenSpinRequest request) {

        return service.spin(userId, request.getBet());
    }
}