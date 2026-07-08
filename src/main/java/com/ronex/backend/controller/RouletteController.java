package com.ronex.backend.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ronex.backend.dto.RoulettePlayRequest;
import com.ronex.backend.dto.RoulettePlayResponse;
import com.ronex.backend.service.RouletteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/games/roulette")
@RequiredArgsConstructor
public class RouletteController {

    private final RouletteService rouletteService;

    @PostMapping("/play")
    public RoulettePlayResponse play(
            @RequestBody RoulettePlayRequest request,
            Principal principal
    ) {
        Long userId = Long.parseLong(principal.getName());
        return rouletteService.play(userId, request);
    }
}