package com.ronex.backend.service;

import com.ronex.backend.dto.ManagerGiftRequestResponse;
import com.ronex.backend.model.AdminUser;
import com.ronex.backend.model.GiftRequest;
import com.ronex.backend.model.User;
import com.ronex.backend.repository.AdminUserRepository;
import com.ronex.backend.repository.GiftRequestRepository;
import com.ronex.backend.repository.UserRepository;
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

    private final UserRepository userRepository;

    private final AdminUserRepository adminUserRepository;


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


    // =========================================================
    // APPROVE
    // =========================================================

    @Override
    @Transactional
    public ManagerGiftRequestResponse approveRequest(
            Long requestId
    ) {

        GiftRequest request =
                giftRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Gift request not found"
                                )
                        );

        if (!"PENDING".equalsIgnoreCase(
                request.getStatus()
        )) {

            throw new RuntimeException(
                    "Only pending gift requests can be approved"
            );
        }

        if (request.getRecipientUserId() == null) {

            throw new RuntimeException(
                    "Gift request recipient is missing"
            );
        }

        if (request.getAmount() == null
                || request.getAmount() <= 0) {

            throw new RuntimeException(
                    "Invalid gift request amount"
            );
        }

        // ---------------------------------------------
        // VERIFY RECIPIENT
        // ---------------------------------------------

        User recipient =
                userRepository
                        .findById(
                                request.getRecipientUserId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recipient user not found"
                                )
                        );

        // ---------------------------------------------
        // CREDIT RECIPIENT
        // ---------------------------------------------

        walletService.credit(
                recipient.getId(),
                request.getAmount().longValue(),
                "GIFT_REQUEST"
        );

        // ---------------------------------------------
        // MARK APPROVED
        // ---------------------------------------------

        request.setStatus("APPROVED");

        GiftRequest saved =
                giftRequestRepository.save(request);

        return toResponse(saved);
    }


    // =========================================================
    // REJECT
    // =========================================================

    @Override
    @Transactional
    public ManagerGiftRequestResponse rejectRequest(
            Long requestId
    ) {

        GiftRequest request =
                giftRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Gift request not found"
                                )
                        );

        if (!"PENDING".equalsIgnoreCase(
                request.getStatus()
        )) {

            throw new RuntimeException(
                    "Only pending gift requests can be rejected"
            );
        }

        request.setStatus("REJECTED");

        GiftRequest saved =
                giftRequestRepository.save(request);

        return toResponse(saved);
    }


    // =========================================================
    // STATUS
    // =========================================================

    private String normalizeStatus(
            String status
    ) {

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


    // =========================================================
    // RESPONSE
    // =========================================================

    private ManagerGiftRequestResponse toResponse(
            GiftRequest request
    ) {

        String agentUsername = null;

        String recipientName = null;
        String recipientPhone = null;


        // ---------------------------------------------
        // AGENT
        // ---------------------------------------------

        if (request.getAgentUserId() != null) {

            AdminUser agent =
                    adminUserRepository
                            .findByUserId(
                                    request.getAgentUserId()
                            )
                            .orElse(null);

            if (agent != null) {

                agentUsername =
                        agent.getUsername();
            }
        }


        // ---------------------------------------------
        // RECIPIENT
        // ---------------------------------------------

        if (request.getRecipientUserId() != null) {

            User recipient =
                    userRepository
                            .findById(
                                    request.getRecipientUserId()
                            )
                            .orElse(null);

            if (recipient != null) {

                recipientName =
                        recipient.getName();

                recipientPhone =
                        recipient.getPhone();
            }
        }


        return new ManagerGiftRequestResponse(
                request.getId(),

                request.getAgentUserId(),
                agentUsername,

                request.getRecipientUserId(),
                recipientName,
                recipientPhone,

                request.getAmount(),

                request.getReason(),
                request.getUrl(),

                request.getStatus(),

                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }
}