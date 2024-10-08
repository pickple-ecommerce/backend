package com.pickple.payment_service.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateRequestEvent {
    private UUID orderId;
    private String userName;
    private BigDecimal amount;
    private String message = "Request payment creation for a new order.";
}
