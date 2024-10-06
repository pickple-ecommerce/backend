package com.pickple.delivery.infrastructure.messaging.events;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryDeleteFailureEvent {

    private UUID deliveryId;

    private UUID orderId;

    private String message;
}
