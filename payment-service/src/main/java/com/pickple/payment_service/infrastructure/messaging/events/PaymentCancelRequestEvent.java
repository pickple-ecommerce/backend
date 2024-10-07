package com.pickple.payment_service.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelRequestEvent {
    private UUID orderId;
    private String message = "Request payment cancellation due to order cancellation.";
}
