package com.pickple.commerceservice.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PaymentCreateRequestEvent {
    private UUID orderId;
    private BigDecimal amount;
    private String username;
}
