package com.pickple.payment_service.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCancelResponseEvent {
    private UUID orderId;
    private UUID paymentId;
    private String status = "CANCELED";

    public PaymentCancelResponseEvent(UUID orderId, UUID paymentId) {
        this.orderId = orderId;
        this.paymentId = paymentId;
    }
}
