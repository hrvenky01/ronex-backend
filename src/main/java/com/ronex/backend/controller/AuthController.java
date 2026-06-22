package com.ronex.backend.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
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
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    // ==============================
    // 🔥 FIREBASE OTP LOGIN (FINAL)
    // ==============================
    @PostMapping("/firebase-login")
    public Map<String, String> firebaseLogin(
            @RequestBody Map<String, String> body
    ) throws Exception {

        String firebaseIdToken = body.get("firebaseToken");

        FirebaseToken decodedToken =
                FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);

        // ✅ CORRECT WAY (Java SDK)
        String phone = (String) decodedToken
                .getClaims()
                .get("phone_number"); // +919703352669

        if (phone == null) {
            throw new RuntimeException("Phone number not found in Firebase token");
        }

        // normalize
        phone = phone.replace("+91", "");

        // create user if needed
        authService.ensureUserExists(phone);

        String role = authService.getUserRole(phone);
        String jwt  = jwtUtil.generateToken(phone, role);

        return Map.of(
                "token", jwt,
                "role", role,
                "phone", phone
        );
    }

    // ======================
    // 🔐 ADMIN LOGIN
    // ======================
    @PostMapping("/admin/login")
    public Map<String, String> adminLogin(
            @RequestBody Map<String, String> req
    ) {

        if ("admin".equals(req.get("username"))
                && "admin123".equals(req.get("password"))) {

            String token = jwtUtil.generateToken("admin", "ADMIN");

            return Map.of(
                    "token", token,
                    "role", "ADMIN",
                    "username", "admin"
            );
        }

        throw new RuntimeException("Invalid Admin Credentials");
    }
}