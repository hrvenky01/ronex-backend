package com.ronex.backend.service;

import com.ronex.backend.dto.ManagerGiftRequestResponse;

import java.util.List;

public interface ManagerGiftRequestService {

    List<ManagerGiftRequestResponse> getAllRequests();

    List<ManagerGiftRequestResponse> getRequestsByStatus(
            String status
    );

    ManagerGiftRequestResponse approveRequest(Long requestId);

    ManagerGiftRequestResponse rejectRequest(Long requestId);
}