package com.ronex.backend.config;

import com.ronex.backend.model.AdminUser;
import com.ronex.backend.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StaffAdminInitializer implements CommandLineRunner {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ronex.initial-admin.username:}")
    private String username;

    @Value("${ronex.initial-admin.password:}")
    private String password;

    public StaffAdminInitializer(
            AdminUserRepository adminUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        System.out.println("========================================");
        System.out.println("RONEX STAFF ADMIN INITIALIZER STARTED");
        System.out.println("Username configured: "
                + (username != null && !username.isBlank()));
        System.out.println("Password configured: "
                + (password != null && !password.isBlank()));
        System.out.println("========================================");

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {

            System.out.println(
                    "ℹ️ Initial ADMIN credentials are not configured. Skipping admin creation."
            );

            return;
        }

        if (adminUserRepository.findByUsername(username).isPresent()) {

            System.out.println(
                    "ℹ️ ADMIN user already exists: " + username
            );

            return;
        }

        AdminUser admin = new AdminUser();

        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole("ADMIN");
        admin.setActive(true);

        adminUserRepository.save(admin);

        System.out.println(
                "✅ Initial ADMIN user created: " + username
        );
    }
}