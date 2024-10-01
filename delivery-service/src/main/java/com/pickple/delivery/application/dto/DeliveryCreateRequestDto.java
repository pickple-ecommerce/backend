package com.pickple.delivery.application.dto;

import com.pickple.delivery.infrastructure.messaging.events.DeliveryCreateRequestEvent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryCreateRequestDto {

    @NotNull
    private UUID orderId;

    private String deliveryRequirement;

    @NotBlank
    private String recipientName;

    @NotBlank
    private String recipientAddress;

    @NotBlank
    private String recipientContact;

    public static DeliveryCreateRequestDto fromEvent(DeliveryCreateRequestEvent event) {
        return DeliveryCreateRequestDto.builder()
                .orderId(event.getOrderId())
                .deliveryRequirement(event.getDeliveryRequirement())
                .recipientName(event.getRecipientName())
                .recipientAddress(event.getRecipientAddress())
                .recipientContact(event.getRecipientContact())
                .build();
    }

}
