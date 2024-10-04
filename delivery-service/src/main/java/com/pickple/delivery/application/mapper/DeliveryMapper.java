package com.pickple.delivery.application.mapper;

import com.pickple.delivery.application.dto.DeliveryCreateRequestDto;
import com.pickple.delivery.application.dto.DeliveryStartRequestDto;
import com.pickple.delivery.application.dto.DeliveryStartResponseDto;
import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.infrastructure.messaging.events.DeliveryCreateRequestEvent;
import com.pickple.delivery.presentation.request.DeliveryStartRequest;
import java.util.UUID;

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

    public static DeliveryStartRequestDto convertStartRequestToDto(UUID deliveryId, DeliveryStartRequest dto) {
        return DeliveryStartRequestDto.builder()
                .deliveryId(deliveryId)
                .carrierName(dto.getCarrierName())
                .deliveryType(dto.getDeliveryType())
                .trackingNumber(dto.getTrackingNumber())
                .build();
    }

    public static DeliveryStartResponseDto convertEntityToStartResponseDto(Delivery delivery) {
        return DeliveryStartResponseDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .carrierName(delivery.getCarrierName())
                .deliveryType(delivery.getDeliveryType().getTypeName())
                .trackingNumber(delivery.getTrackingNumber())
                .orderId(delivery.getOrderId())
                .deliveryStatus(delivery.getDeliveryStatus().getStatus())
                .deliveryRequirement(delivery.getDeliveryRequirement())
                .recipientName(delivery.getRecipientName())
                .recipientAddress(delivery.getRecipientAddress())
                .recipientContact(delivery.getRecipientContact())
                .build();
    }

}

