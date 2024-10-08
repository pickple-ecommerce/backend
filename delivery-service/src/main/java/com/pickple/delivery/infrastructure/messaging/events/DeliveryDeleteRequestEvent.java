package com.pickple.delivery.infrastructure.messaging.events;

import java.util.UUID;
import lombok.Getter;

@Getter
public class DeliveryDeleteRequestEvent {

    private String deleter;

    private UUID deliveryId;

    private UUID orderId;

}
