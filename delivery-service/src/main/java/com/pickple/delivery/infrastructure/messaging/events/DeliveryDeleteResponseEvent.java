package com.pickple.delivery.infrastructure.messaging.events;

import java.util.UUID;
import lombok.Getter;

@Getter
public class DeliveryDeleteResponseEvent {

    private UUID deliveryId;

    private UUID orderId;

}
