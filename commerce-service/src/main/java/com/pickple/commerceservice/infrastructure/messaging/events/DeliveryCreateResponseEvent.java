package com.pickple.commerceservice.infrastructure.messaging.events;

import java.util.UUID;
import lombok.Getter;

@Getter
public class DeliveryCreateResponseEvent {

    private UUID orderId;

    private UUID deliveryId;

}