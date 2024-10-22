package com.pickple.delivery.infrastructure.messaging.events;

import java.util.UUID;
import lombok.Getter;

@Getter
public class DeliveryCreateRequestEvent {

    private UUID orderId;

    private String username;

    private String deliveryRequirement;

    private String recipientName;

    private String recipientAddress;

    private String recipientContact;

}
