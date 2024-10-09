package com.pickple.commerceservice.infrastructure.messaging.events;

import java.util.UUID;
import lombok.Getter;

@Getter
public class DeliveryCreateRequestEvent {
    private UUID orderId;
    private String deliveryRequirement;
    private String recipientName;
    private String recipientAddress;
    private String recipientContact;

    public DeliveryCreateRequestEvent(UUID orderId, String deliveryRequirement, String recipientName, String recipientAddress, String recipientContact) {
        this.orderId = orderId;
        this.deliveryRequirement = deliveryRequirement;
        this.recipientName = recipientName;
        this.recipientAddress = recipientAddress;
        this.recipientContact = recipientContact;
    }
}
