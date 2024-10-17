package com.pickple.delivery.application.events;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryEndEvent {

    private UUID orderId;

    private UUID deliveryId;

}
