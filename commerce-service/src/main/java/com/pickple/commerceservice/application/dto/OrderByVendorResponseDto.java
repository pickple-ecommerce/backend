package com.pickple.commerceservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderByVendorResponseDto {
    private UUID orderId;
    private String username;
    private BigDecimal amount;
    private String orderStatus;
    private UUID productId;
    private Long orderQuantity;
}
