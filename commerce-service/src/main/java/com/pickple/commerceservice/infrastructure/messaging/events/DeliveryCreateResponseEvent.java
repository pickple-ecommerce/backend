package com.pickple.commerceservice.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryCreateResponseEvent {
    private UUID orderId;
    private UUID deliveryId;
}