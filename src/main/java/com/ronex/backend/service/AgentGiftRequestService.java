package com.ronex.backend.service;

import com.ronex.backend.dto.AgentGiftRequestCreateRequest;
import com.ronex.backend.dto.AgentGiftRequestResponse;

import java.util.List;

public interface AgentGiftRequestService {

    AgentGiftRequestResponse createRequest(
            String username,
            AgentGiftRequestCreateRequest request
    );

    List<AgentGiftRequestResponse> getMyRequests(
            String username
    );
}