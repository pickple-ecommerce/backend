package com.pickple.delivery.application.mapper;

import com.pickple.delivery.application.dto.DeliveryCreateRequestDto;
import com.pickple.delivery.application.dto.DeliveryCreateResponseDto;
import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.infrastructure.messaging.events.DeliveryCreateRequestEvent;

public class DeliveryMapper {

    public static DeliveryCreateRequestDto convertCreateRequestEventToDto(DeliveryCreateRequestEvent event) {
        return DeliveryCreateRequestDto.builder()
                .orderId(event.getOrderId())
                .deliveryRequirement(event.getDeliveryRequirement())
                .recipientName(event.getRecipientName())
                .recipientAddress(event.getRecipientAddress())
                .recipientContact(event.getRecipientContact())
                .build();
    }

    public static DeliveryCreateResponseDto convertEntityToCreateResponseDto (Delivery delivery) {
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

