package com.pickple.payment_service.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCanceledEvent {
    private UUID orderId;
    private String status;
}
