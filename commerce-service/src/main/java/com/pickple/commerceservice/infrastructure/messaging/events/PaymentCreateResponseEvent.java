package com.pickple.commerceservice.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateResponseEvent {
    private UUID orderId;
    private UUID paymentId;
    private String method;
    private String status;
}