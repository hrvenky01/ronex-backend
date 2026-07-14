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

        System.out.println("================================");
        System.out.println("🔥 FIREBASE LOGIN HIT");
        System.out.println("================================");

        String firebaseIdToken = body.get("firebaseToken");

        if (firebaseIdToken == null || firebaseIdToken.isEmpty()) {
            throw new RuntimeException("Firebase token is missing");
        }

        System.out.println("✅ Firebase Token Received");

        FirebaseToken decodedToken =
                FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);

        System.out.println("✅ Firebase Token Verified");

        String phone = (String) decodedToken.getClaims().get("phone_number");

        System.out.println("PHONE = " + phone);

        if (phone == null) {
            throw new RuntimeException("Phone number not found in Firebase token");
        }

        phone = phone.replace("+91", "");

        authService.ensureUserExists(phone);

        System.out.println("✅ User Ready");

        String role = authService.getUserRole(phone);

        System.out.println("ROLE = " + role);

        String jwt = jwtUtil.generateToken(phone, role);

        System.out.println("✅ JWT GENERATED");

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