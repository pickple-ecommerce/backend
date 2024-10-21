package com.pickple.commerceservice.infrastructure.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentClientDto {
    private UUID orderId;
    private UUID paymentId;
    private String userName;
    private BigDecimal amount;
    private String method;
    private String status;
}
