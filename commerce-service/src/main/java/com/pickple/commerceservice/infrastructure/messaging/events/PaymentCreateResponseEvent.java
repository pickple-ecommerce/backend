package com.pickple.commerceservice.infrastructure.messaging.events;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PaymentCreateResponseEvent {
    private UUID orderId;
    private UUID paymentId;
    private String method;
    private String status;

    public PaymentCreateResponseEvent(UUID orderId, UUID paymentId, String method, String status) {
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.method = method;
        this.status = status;
    }
}