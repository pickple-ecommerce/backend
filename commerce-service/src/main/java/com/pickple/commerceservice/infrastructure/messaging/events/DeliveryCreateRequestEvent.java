package com.pickple.commerceservice.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryCreateRequestEvent {
    private UUID orderId;
    private String deliveryRequirement;
    private String recipientName;
    private String recipientAddress;
    private String recipientContact;
    private String username;
}