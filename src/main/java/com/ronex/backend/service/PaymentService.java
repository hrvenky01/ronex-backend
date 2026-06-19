package com.ronex.backend.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.ronex.backend.dto.CreateOrderRequest;
import com.ronex.backend.dto.OrderResponse;
import com.ronex.backend.repository.UserRepository;

@Service
public class PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private UserRepository userRepository;

    public OrderResponse createOrder(CreateOrderRequest req) throws Exception {

        JSONObject options = new JSONObject();
        options.put("amount", req.amount * 100); // paise
        options.put("currency", "INR");
        options.put("receipt", "order_rcptid_" + System.currentTimeMillis());

        Order order = razorpayClient.orders.create(options);

        OrderResponse res = new OrderResponse();
        res.orderId = order.get("id");
        res.amount = req.amount;
        res.currency = "INR";

        return res;
    }
}