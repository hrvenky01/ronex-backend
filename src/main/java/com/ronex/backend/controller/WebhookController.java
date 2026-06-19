package com.ronex.backend.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ronex.backend.model.PaymentOrder;
import com.ronex.backend.model.User;
import com.ronex.backend.repository.PaymentOrderRepository;
import com.ronex.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/payment")
public class WebhookController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentOrderRepository orderRepository;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload) {

        try {
            JSONObject json = new JSONObject(payload);

            String event = json.getString("event");

            if ("payment.captured".equals(event)) {

                JSONObject payment = json.getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

                String orderId = payment.getString("order_id");
                String paymentId = payment.getString("id");

                int amount = payment.getInt("amount") / 100;

                // 🔥 STEP 1: FIND ORDER
                PaymentOrder order = orderRepository.findByOrderId(orderId);

                if (order == null || "PAID".equals(order.getStatus())) {
                    return ResponseEntity.ok("ignored");
                }

                // 🔥 STEP 2: FIND USER FROM ORDER
                User user = userRepository.findById(order.getUserId())
                        .orElseThrow();

                // 🔥 STEP 3: ADD COINS (SAFE)
                user.setCoins(user.getCoins() + order.getCoins());

                userRepository.save(user);

                // 🔥 STEP 4: MARK ORDER PAID
                order.setStatus("PAID");
                orderRepository.save(order);

                System.out.println("Payment Success: " + paymentId);
            }

            return ResponseEntity.ok("success");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("error");
        }
    }
}