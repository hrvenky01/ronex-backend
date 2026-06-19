// GreedyWheelController.java
package com.ronex.backend.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ronex.backend.dto.GreedySpinRequest;
import com.ronex.backend.dto.GreedySpinResponse;
import com.ronex.backend.service.GreedyWheelService;

@RestController
@RequestMapping("/api/games/greedy-wheel")
public class GreedyWheelController {

    private final GreedyWheelService service;

    public GreedyWheelController(GreedyWheelService service) {
        this.service = service;
    }

    @PostMapping("/spin")
    public GreedySpinResponse spin(@RequestBody GreedySpinRequest request) {
        return service.spin(request.getUserId(), request.getBetAmount());
    }
}