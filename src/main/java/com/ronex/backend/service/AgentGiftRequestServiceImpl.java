package com.ronex.backend.service;

import com.ronex.backend.dto.AgentGiftRecipientResponse;
import com.ronex.backend.dto.AgentGiftRequestCreateRequest;
import com.ronex.backend.dto.AgentGiftRequestResponse;
import com.ronex.backend.model.AdminUser;
import com.ronex.backend.model.GiftRequest;
import com.ronex.backend.model.User;
import com.ronex.backend.repository.AdminUserRepository;
import com.ronex.backend.repository.GiftRequestRepository;
import com.ronex.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentGiftRequestServiceImpl
        implements AgentGiftRequestService {

    private final AdminUserRepository adminUserRepository;
    private final GiftRequestRepository giftRequestRepository;
    private final UserRepository userRepository;

    // =========================================================
    // GET RECIPIENT USERS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<AgentGiftRecipientResponse> getRecipients() {

        return userRepository.findAll()
                .stream()
                .map(user ->
                        new AgentGiftRecipientResponse(
                                user.getId(),
                                user.getName(),
                                user.getPhone()
                        )
                )
                .toList();
    }

    // =========================================================
    // CREATE REQUEST
    // =========================================================

    @Override
    @Transactional
    public AgentGiftRequestResponse createRequest(
            String username,
            AgentGiftRequestCreateRequest request
    ) {

        // ---------------------------------------------
        // FIND LOGGED-IN AGENT
        // ---------------------------------------------

        AdminUser agent =
                adminUserRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Agent account not found"
                                )
                        );

        // ---------------------------------------------
        // CHECK ROLE
        // ---------------------------------------------

        if (!"AGENT".equalsIgnoreCase(agent.getRole())) {

            throw new RuntimeException(
                    "Only AGENT accounts can create gift requests"
            );
        }

        // ---------------------------------------------
        // CHECK ACTIVE
        // ---------------------------------------------

        if (!agent.isActive()) {

            throw new RuntimeException(
                    "Agent account is inactive"
            );
        }

        // ---------------------------------------------
        // CHECK AGENT LINK
        // ---------------------------------------------

        if (agent.getUserId() == null) {

            throw new RuntimeException(
                    "Agent is not linked to a user account"
            );
        }

        // ---------------------------------------------
        // CHECK REQUEST
        // ---------------------------------------------

        if (request == null) {

            throw new RuntimeException(
                    "Request data is required"
            );
        }

        // ---------------------------------------------
        // RECIPIENT
        // ---------------------------------------------

        if (request.getRecipientUserId() == null) {

            throw new RuntimeException(
                    "Recipient user is required"
            );
        }

        User recipient =
                userRepository
                        .findById(request.getRecipientUserId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recipient user not found"
                                )
                        );

        // ---------------------------------------------
        // AMOUNT
        // ---------------------------------------------

        if (request.getAmount() == null
                || request.getAmount() <= 0) {

            throw new RuntimeException(
                    "Amount must be greater than zero"
            );
        }

        // ---------------------------------------------
        // CREATE REQUEST
        // ---------------------------------------------

        GiftRequest giftRequest =
                new GiftRequest();

        // Agent who created request
        giftRequest.setAgentUserId(
                agent.getUserId()
        );

        // Recipient who gets coins
        giftRequest.setRecipientUserId(
                recipient.getId()
        );

        // Old database compatibility
        giftRequest.setUserId(
                recipient.getId()
        );

        giftRequest.setAmount(
                request.getAmount()
        );

        giftRequest.setReason(
                request.getReason() == null
                        ? null
                        : request.getReason().trim()
        );

        // URL intentionally NOT set.

        giftRequest.setStatus("PENDING");

        GiftRequest saved =
                giftRequestRepository.save(
                        giftRequest
                );

        return toResponse(saved);
    }

    // =========================================================
    // MY REQUESTS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<AgentGiftRequestResponse> getMyRequests(
            String username
    ) {

        AdminUser agent =
                adminUserRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Agent account not found"
                                )
                        );

        if (!"AGENT".equalsIgnoreCase(
                agent.getRole()
        )) {

            throw new RuntimeException(
                    "Only AGENT accounts can access these requests"
            );
        }

        if (agent.getUserId() == null) {

            throw new RuntimeException(
                    "Agent is not linked to a user account"
            );
        }

        return giftRequestRepository
                .findByAgentUserIdOrderByCreatedAtDesc(
                        agent.getUserId()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // RESPONSE
    // =========================================================

    private AgentGiftRequestResponse toResponse(
            GiftRequest request
    ) {

        String recipientName = null;
        String recipientPhone = null;

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

        return new AgentGiftRequestResponse(

                request.getId(),

                request.getAgentUserId(),

                request.getRecipientUserId(),

                recipientName,

                recipientPhone,

                request.getAmount(),

                request.getReason(),

                request.getStatus(),

                request.getCreatedAt(),

                request.getUpdatedAt()
        );
    }
}