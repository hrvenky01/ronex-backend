package com.ronex.backend.service;

import com.ronex.backend.model.AdminUser;
import com.ronex.backend.repository.AdminUserRepository;
import com.ronex.backend.security.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StaffAuthServiceImpl implements StaffAuthService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public StaffAuthServiceImpl(
            AdminUserRepository adminUserRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Map<String, String> login(String username, String password) {

        AdminUser staff = adminUserRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Invalid username or password")
                );

        if (!staff.isActive()) {
            throw new RuntimeException("Account is inactive");
        }

        if (!passwordEncoder.matches(password, staff.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(
                staff.getUsername(),
                staff.getRole()
        );

        return Map.of(
                "token", token,
                "username", staff.getUsername(),
                "role", staff.getRole()
        );
    }
}