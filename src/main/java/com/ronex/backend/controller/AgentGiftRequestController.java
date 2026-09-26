package com.ronex.backend.controller;

import com.ronex.backend.dto.AgentGiftRequestCreateRequest;
import com.ronex.backend.dto.AgentGiftRequestResponse;
import com.ronex.backend.service.AgentGiftRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent/gifts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AgentGiftRequestController {

    private final AgentGiftRequestService agentGiftRequestService;

    @PostMapping
    public ResponseEntity<?> createRequest(
            @RequestBody AgentGiftRequestCreateRequest request,
            Authentication authentication
    ) {

        try {

            String username =
                    authentication.getName();

            AgentGiftRequestResponse response =
                    agentGiftRequestService.createRequest(
                            username,
                            request
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getMyRequests(
            Authentication authentication
    ) {

        try {

            String username =
                    authentication.getName();

            List<AgentGiftRequestResponse> response =
                    agentGiftRequestService.getMyRequests(
                            username
                    );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}