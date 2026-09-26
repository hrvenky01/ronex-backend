package com.ronex.backend.service;

import com.ronex.backend.dto.AgentGiftRequestCreateRequest;
import com.ronex.backend.dto.AgentGiftRequestResponse;
import com.ronex.backend.model.AdminUser;
import com.ronex.backend.model.GiftRequest;
import com.ronex.backend.repository.AdminUserRepository;
import com.ronex.backend.repository.GiftRequestRepository;
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

    @Override
    @Transactional
    public AgentGiftRequestResponse createRequest(
            String username,
            AgentGiftRequestCreateRequest request
    ) {

        AdminUser agent =
                adminUserRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Agent account not found"
                                )
                        );

        if (!"AGENT".equalsIgnoreCase(agent.getRole())) {
            throw new RuntimeException(
                    "Only AGENT accounts can create gift requests"
            );
        }

        if (!agent.isActive()) {
            throw new RuntimeException(
                    "Agent account is inactive"
            );
        }

        if (agent.getUserId() == null) {
            throw new RuntimeException(
                    "Agent is not linked to a user account"
            );
        }

        if (request == null) {
            throw new RuntimeException(
                    "Request data is required"
            );
        }

        if (request.getAmount() == null
                || request.getAmount() <= 0) {

            throw new RuntimeException(
                    "Amount must be greater than zero"
            );
        }

        if (request.getUrl() == null
                || request.getUrl().isBlank()) {

            throw new RuntimeException(
                    "URL is required"
            );
        }

        String url = request.getUrl().trim();

        if (!isValidUrl(url)) {
            throw new RuntimeException(
                    "Please enter a valid URL"
            );
        }

        GiftRequest giftRequest =
                new GiftRequest();

        giftRequest.setUserId(agent.getUserId());
        giftRequest.setAmount(request.getAmount());
        giftRequest.setReason(
                request.getReason() == null
                        ? null
                        : request.getReason().trim()
        );
        giftRequest.setUrl(url);
        giftRequest.setStatus("PENDING");

        GiftRequest saved =
                giftRequestRepository.save(giftRequest);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentGiftRequestResponse> getMyRequests(
            String username
    ) {

        AdminUser agent =
                adminUserRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Agent account not found"
                                )
                        );

        if (!"AGENT".equalsIgnoreCase(agent.getRole())) {
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
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .filter(request ->
                        agent.getUserId()
                                .equals(request.getUserId())
                )
                .map(this::toResponse)
                .toList();
    }

    private boolean isValidUrl(String url) {

        return url.startsWith("http://")
                || url.startsWith("https://");
    }

    private AgentGiftRequestResponse toResponse(
            GiftRequest request
    ) {

        return new AgentGiftRequestResponse(
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