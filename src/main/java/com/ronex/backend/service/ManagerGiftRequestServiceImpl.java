package com.ronex.backend.service;

import com.ronex.backend.dto.ManagerGiftRequestResponse;
import com.ronex.backend.model.GiftRequest;
import com.ronex.backend.repository.GiftRequestRepository;
import com.ronex.backend.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerGiftRequestServiceImpl
        implements ManagerGiftRequestService {

    private final GiftRequestRepository giftRequestRepository;
    private final WalletService walletService;

    @Override
    @Transactional(readOnly = true)
    public List<ManagerGiftRequestResponse> getAllRequests() {

        return giftRequestRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagerGiftRequestResponse> getRequestsByStatus(
            String status
    ) {

        String normalizedStatus =
                normalizeStatus(status);

        return giftRequestRepository
                .findByStatusOrderByCreatedAtDesc(
                        normalizedStatus
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ManagerGiftRequestResponse approveRequest(
            Long requestId
    ) {

        GiftRequest request =
                giftRequestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Gift request not found"
                                )
                        );

        if (!"PENDING".equalsIgnoreCase(request.getStatus())) {
            throw new RuntimeException(
                    "Only pending gift requests can be approved"
            );
        }

        if (request.getUserId() == null) {
            throw new RuntimeException(
                    "Gift request user is missing"
            );
        }

        if (request.getAmount() == null
                || request.getAmount() <= 0) {

            throw new RuntimeException(
                    "Invalid gift request amount"
            );
        }

        /*
         * IMPORTANT:
         *
         * Approval credits the requested coins
         * to the user's wallet.
         *
         * WalletService also creates the wallet
         * transaction record.
         */
        walletService.credit(
                request.getUserId(),
                request.getAmount().longValue(),
                "GIFT_REQUEST"
        );

        request.setStatus("APPROVED");

        GiftRequest saved =
                giftRequestRepository.save(request);

        return toResponse(saved);
    }

    @Override
    @Transactional
    public ManagerGiftRequestResponse rejectRequest(
            Long requestId
    ) {

        GiftRequest request =
                giftRequestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Gift request not found"
                                )
                        );

        if (!"PENDING".equalsIgnoreCase(request.getStatus())) {
            throw new RuntimeException(
                    "Only pending gift requests can be rejected"
            );
        }

        request.setStatus("REJECTED");

        GiftRequest saved =
                giftRequestRepository.save(request);

        return toResponse(saved);
    }

    private String normalizeStatus(String status) {

        if (status == null || status.isBlank()) {
            throw new RuntimeException(
                    "Status is required"
            );
        }

        String normalized =
                status.trim().toUpperCase();

        if (!normalized.equals("PENDING")
                && !normalized.equals("APPROVED")
                && !normalized.equals("REJECTED")) {

            throw new RuntimeException(
                    "Invalid gift request status"
            );
        }

        return normalized;
    }

    private ManagerGiftRequestResponse toResponse(
            GiftRequest request
    ) {

        return new ManagerGiftRequestResponse(
                request.getId(),
                request.getUserId(),
                request.getAmount(),
                request.getReason(),
                request.getUrl(),
                request.getStatus(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }
}