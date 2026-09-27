package com.ronex.backend.controller;

import com.ronex.backend.model.AdminUser;
import com.ronex.backend.model.User;
import com.ronex.backend.repository.AdminUserRepository;
import com.ronex.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/agents")
@CrossOrigin("*")
public class AdminAgentsController {

    private final AdminUserRepository adminUserRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAgentsController(
            AdminUserRepository adminUserRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.adminUserRepository = adminUserRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // GET ALL AGENTS
    // =========================

    @GetMapping
    public ResponseEntity<List<AdminUser>> getAgents() {

        List<AdminUser> agents =
                adminUserRepository.findAll()
                        .stream()
                        .filter(user ->
                                "AGENT".equalsIgnoreCase(
                                        user.getRole()
                                )
                        )
                        .toList();

        return ResponseEntity.ok(agents);
    }

    // =========================
    // GET RONEX USERS
    // =========================

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {

        return ResponseEntity.ok(
                userRepository.findAll()
        );
    }

    // =========================
    // CREATE AGENT
    // =========================

    @PostMapping
    public ResponseEntity<?> createAgent(
            @RequestBody CreateAgentRequest request
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
                    .body(
                            "Password must be at least 8 characters"
                    );
        }

        if (request.userId() == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Ronex user is required");
        }

        String username =
                request.username().trim();

        // Check username
        if (adminUserRepository
                .findByUsername(username)
                .isPresent()) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        // Check Ronex user
        User ronexUser =
                userRepository.findById(request.userId())
                        .orElse(null);

        if (ronexUser == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Ronex user not found");
        }

        // Prevent duplicate agent mapping
        if (adminUserRepository
                .findByUserId(request.userId())
                .isPresent()) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                            "This Ronex user is already linked to an agent"
                    );
        }

        AdminUser agent =
                new AdminUser();

        agent.setUsername(username);

        agent.setPassword(
                passwordEncoder.encode(
                        request.password()
                )
        );

        agent.setRole("AGENT");

        agent.setUserId(
                request.userId()
        );

        agent.setActive(true);

        AdminUser savedAgent =
                adminUserRepository.save(agent);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedAgent);
    }

    // =========================
    // ACTIVATE / DEACTIVATE
    // =========================

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateAgentStatus(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {

        return adminUserRepository.findById(id)
                .map(agent -> {

                    if (!"AGENT".equalsIgnoreCase(
                            agent.getRole()
                    )) {

                        return ResponseEntity
                                .badRequest()
                                .body(
                                        "Selected user is not an agent"
                                );
                    }

                    agent.setActive(active);

                    AdminUser updated =
                            adminUserRepository.save(agent);

                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body("Agent not found")
                );
    }

    // =========================
    // REQUEST DTO
    // =========================

    public record CreateAgentRequest(
            String username,
            String password,
            Long userId
    ) {
    }
}