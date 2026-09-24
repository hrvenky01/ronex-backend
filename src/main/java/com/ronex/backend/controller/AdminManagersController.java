package com.ronex.backend.controller;

import com.ronex.backend.dto.CreateManagerRequest;
import com.ronex.backend.dto.ManagerResponse;
import com.ronex.backend.model.AdminUser;
import com.ronex.backend.repository.AdminUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/managers")
@CrossOrigin("*")
public class AdminManagersController {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminManagersController(
            AdminUserRepository adminUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // GET ALL MANAGERS
    // =========================
    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getManagers() {

        List<ManagerResponse> managers = adminUserRepository.findAll()
                .stream()
                .filter(user -> "MANAGER".equalsIgnoreCase(user.getRole()))
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(managers);
    }

    // =========================
    // CREATE MANAGER
    // =========================
    @PostMapping
    public ResponseEntity<?> createManager(
            @RequestBody CreateManagerRequest request
    ) {

        if (request.username() == null
                || request.username().isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body("Username is required");
        }

        if (request.password() == null
                || request.password().isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body("Password is required");
        }

        if (request.password().length() < 8) {

            return ResponseEntity
                    .badRequest()
                    .body("Password must be at least 8 characters");
        }

        String username = request.username().trim();

        if (adminUserRepository.findByUsername(username).isPresent()) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        AdminUser manager = new AdminUser();

        manager.setUsername(username);

        manager.setPassword(
                passwordEncoder.encode(request.password())
        );

        // IMPORTANT:
        // Admin endpoint can ONLY create MANAGER.
        manager.setRole("MANAGER");

        manager.setActive(true);

        AdminUser savedManager =
                adminUserRepository.save(manager);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(savedManager));
    }

    // =========================
    // ACTIVATE / DEACTIVATE
    // =========================
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateManagerStatus(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {

        return adminUserRepository.findById(id)
                .map(manager -> {

                    if (!"MANAGER".equalsIgnoreCase(manager.getRole())) {

                        return ResponseEntity
                                .badRequest()
                                .body("Selected user is not a manager");
                    }

                    manager.setActive(active);

                    AdminUser updated =
                            adminUserRepository.save(manager);

                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body("Manager not found")
                );
    }

    // =========================
    // RESPONSE MAPPER
    // =========================
    private ManagerResponse toResponse(AdminUser manager) {

        return new ManagerResponse(
                manager.getId(),
                manager.getUsername(),
                manager.getRole(),
                manager.isActive(),
                manager.getCreatedAt()
        );
    }
}