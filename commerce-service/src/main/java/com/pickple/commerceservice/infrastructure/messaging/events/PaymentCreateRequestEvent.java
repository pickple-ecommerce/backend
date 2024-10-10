package com.pickple.commerceservice.infrastructure.messaging.events;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class PaymentCreateRequestEvent {
    private UUID orderId;
    private BigDecimal amount;
    private String username;

    public PaymentCreateRequestEvent(UUID orderId, BigDecimal amount, String username) {
        this.orderId = orderId;
        this.amount = amount;
        this.username = username;
    }
}
