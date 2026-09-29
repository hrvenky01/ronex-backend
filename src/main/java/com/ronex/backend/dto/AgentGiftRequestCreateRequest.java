package com.ronex.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentGiftRequestCreateRequest {

    private Long recipientUserId;

    private Integer amount;

    private String reason;

    private String url;
}