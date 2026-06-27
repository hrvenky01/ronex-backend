package com.ronex.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ronex.backend.dto.CreateOrderRequest;
import com.ronex.backend.dto.OrderResponse;
import com.ronex.backend.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    //@Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public OrderResponse createOrder(@RequestBody CreateOrderRequest req) throws Exception {
        return paymentService.createOrder(req);
    }
}
