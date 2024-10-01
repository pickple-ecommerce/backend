package com.pickple.delivery.application.dto;

import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import java.util.UUID;
import lombok.Builder;

@Builder
public class DeliveryCreateResponseDto {

    private UUID deliveryId;

    private UUID orderId;

    private DeliveryStatus deliveryStatus;

    private String deliveryRequirement;

    private String recipientName;

    private String recipientAddress;

    private String recipientContact;

    public static DeliveryCreateResponseDto fromEntity(Delivery delivery) {
        return DeliveryCreateResponseDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .orderId(delivery.getOrderId())
                .deliveryStatus(delivery.getDeliveryStatus())
                .deliveryRequirement(delivery.getDeliveryRequirement())
                .recipientName(delivery.getRecipientName())
                .recipientAddress(delivery.getRecipientAddress())
                .recipientContact(delivery.getRecipientContact())
                .build();
    }

}