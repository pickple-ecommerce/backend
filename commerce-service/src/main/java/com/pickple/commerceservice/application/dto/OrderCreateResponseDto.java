package com.pickple.commerceservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateResponseDto {

    private UUID orderId;
    private String username;
    private BigDecimal amount;
    private String orderStatus;

    private List<OrderDetailResponseDto> orderDetails;

}
