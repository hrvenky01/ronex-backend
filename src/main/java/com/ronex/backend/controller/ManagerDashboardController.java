package com.ronex.backend.controller;

import com.ronex.backend.dto.ManagerDashboardStatsResponse;
import com.ronex.backend.repository.GiftRequestRepository;
import com.ronex.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/manager/dashboard")
@CrossOrigin("*")
public class ManagerDashboardController {

    private final GiftRequestRepository giftRequestRepository;
    private final UserRepository userRepository;

    public ManagerDashboardController(
            GiftRequestRepository giftRequestRepository,
            UserRepository userRepository
    ) {
        this.giftRequestRepository = giftRequestRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<ManagerDashboardStatsResponse> getStats() {

        LocalDate today = LocalDate.now();

        LocalDateTime startOfToday =
                today.atStartOfDay();

        LocalDateTime startOfTomorrow =
                today.plusDays(1).atStartOfDay();

        long pendingGifts =
                giftRequestRepository.countByStatus("PENDING");

        long approvedToday =
                giftRequestRepository
                        .countByStatusAndUpdatedAtGreaterThanEqualAndUpdatedAtLessThan(
                                "APPROVED",
                                startOfToday,
                                startOfTomorrow
                        );

        long rejectedToday =
                giftRequestRepository
                        .countByStatusAndUpdatedAtGreaterThanEqualAndUpdatedAtLessThan(
                                "REJECTED",
                                startOfToday,
                                startOfTomorrow
                        );

        long totalAgents =
                userRepository.countByRole("AGENT");

        return ResponseEntity.ok(
                new ManagerDashboardStatsResponse(
                        pendingGifts,
                        approvedToday,
                        rejectedToday,
                        totalAgents
                )
        );
    }
}