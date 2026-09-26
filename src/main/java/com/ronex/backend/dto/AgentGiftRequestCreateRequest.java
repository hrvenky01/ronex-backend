package com.ronex.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentGiftRequestCreateRequest {

    private Integer amount;

    private String reason;

    private String url;
}