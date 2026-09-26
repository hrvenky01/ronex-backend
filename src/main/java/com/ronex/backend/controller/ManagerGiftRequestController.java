package com.ronex.backend.controller;

import com.ronex.backend.dto.ManagerGiftRequestResponse;
import com.ronex.backend.service.ManagerGiftRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/gifts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ManagerGiftRequestController {

    private final ManagerGiftRequestService giftRequestService;

    /**
     * Get all gift requests.
     */
    @GetMapping
    public ResponseEntity<List<ManagerGiftRequestResponse>>
    getAllRequests() {

        return ResponseEntity.ok(
                giftRequestService.getAllRequests()
        );
    }

    /**
     * Get requests by status.
     *
     * Example:
     * /api/manager/gifts?status=PENDING
     */
    @GetMapping(params = "status")
    public ResponseEntity<?> getRequestsByStatus(
            @RequestParam String status
    ) {

        try {

            return ResponseEntity.ok(
                    giftRequestService
                            .getRequestsByStatus(status)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Approve a pending gift request.
     */
    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(
            @PathVariable Long id
    ) {

        try {

            return ResponseEntity.ok(
                    giftRequestService
                            .approveRequest(id)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    /**
     * Reject a pending gift request.
     */
    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> rejectRequest(
            @PathVariable Long id
    ) {

        try {

            return ResponseEntity.ok(
                    giftRequestService
                            .rejectRequest(id)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}