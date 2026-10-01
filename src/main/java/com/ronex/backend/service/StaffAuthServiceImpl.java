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

        String cleanUsername =
                username == null ? "" : username.trim();

        System.out.println(
                "STAFF LOGIN ATTEMPT username=[" +
                cleanUsername + "]"
        );

        AdminUser staff = adminUserRepository
                .findByUsername(cleanUsername)
                .orElseThrow(() -> {
                    System.out.println(
                            "STAFF LOGIN FAILED: USER NOT FOUND"
                    );
                    return new RuntimeException(
                            "Invalid username or password"
                    );
                });

        System.out.println(
                "STAFF FOUND: id=" + staff.getId()
                        + ", username=" + staff.getUsername()
                        + ", role=" + staff.getRole()
                        + ", active=" + staff.isActive()
                        + ", userId=" + staff.getUserId()
        );

        if (!staff.isActive()) {
            System.out.println(
                    "STAFF LOGIN FAILED: ACCOUNT INACTIVE"
            );

            throw new RuntimeException(
                    "Account is inactive"
            );
        }

        boolean passwordMatches =
                password != null &&
                passwordEncoder.matches(
                        password,
                        staff.getPassword()
                );

        System.out.println(
                "STAFF PASSWORD MATCH=" +
                passwordMatches
        );

        if (!passwordMatches) {
            System.out.println(
                    "STAFF LOGIN FAILED: PASSWORD MISMATCH"
            );

            throw new RuntimeException(
                    "Invalid username or password"
            );
        }

        String role =
                staff.getRole() == null
                        ? ""
                        : staff.getRole().trim().toUpperCase();

        String token = jwtUtil.generateToken(
                staff.getUsername(),
                role
        );

        System.out.println(
                "STAFF LOGIN SUCCESS: role=" + role
        );

        return Map.of(
                "token", token,
                "username", staff.getUsername(),
                "role", role
        );
    }
}