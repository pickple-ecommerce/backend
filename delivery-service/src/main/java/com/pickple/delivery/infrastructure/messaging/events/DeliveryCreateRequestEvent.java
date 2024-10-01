package com.pickple.delivery.infrastructure.messaging.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;

@Getter
public class DeliveryCreateRequestEvent {

    @NotNull
    private UUID orderId;

    private String deliveryRequirement;

    @NotBlank
    private String recipientName;

    @NotBlank
    private String recipientAddress;

    @NotBlank
    private String recipientContact;

}
