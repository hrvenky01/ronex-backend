package com.ronex.backend.controller;

import com.ronex.backend.dto.SendOtpRequest;
import com.ronex.backend.dto.VerifyOtpRequest;
import com.ronex.backend.security.JwtUtil;
import com.ronex.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    // ======================
    // 📱 USER OTP LOGIN
    // ======================
    @PostMapping("/send-otp")
    public String sendOtp(@RequestBody SendOtpRequest req) {
        return authService.sendOtp(req.getPhone());
    }

    @PostMapping("/verify-otp")
    public Map<String, String> verifyOtp(@RequestBody VerifyOtpRequest req) {

        // OTP verify
        authService.verifyOtp(req.getPhone(), req.getOtp());

        // check role from DB (IMPORTANT for agent system ready)
        String role = authService.getUserRole(req.getPhone()); 
        // returns USER / AGENT

        String token = jwtUtil.generateToken(req.getPhone(), role);

        return Map.of(
                "token", token,
                "role", role,
                "phone", req.getPhone()
        );
    }

    // ======================
    // 🔐 ADMIN LOGIN
    // ======================
    @PostMapping("/admin/login")
    public Map<String, String> adminLogin(@RequestBody Map<String, String> req) {

        String username = req.get("username");
        String password = req.get("password");

        if ("admin".equals(username) && "admin123".equals(password)) {

            String token = jwtUtil.generateToken(username, "ADMIN");

            return Map.of(
                    "token", token,
                    "role", "ADMIN",
                    "username", username
            );
        }

        return Map.of("error", "Invalid Admin Credentials");
    }
}