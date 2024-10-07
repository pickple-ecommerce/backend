package com.pickple.delivery.application.mapper;

import com.pickple.delivery.application.dto.request.DeliveryCreateRequestDto;
import com.pickple.delivery.application.dto.DeliveryDetailInfoDto;
import com.pickple.delivery.application.dto.DeliveryInfoDto;
import com.pickple.delivery.application.dto.response.DeliveryInfoResponseDto;
import com.pickple.delivery.application.dto.request.DeliveryStartRequestDto;
import com.pickple.delivery.application.dto.response.DeliveryStartResponseDto;
import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.domain.repository.projection.DeliveryInfoProjection;
import com.pickple.delivery.infrastructure.messaging.events.DeliveryCreateRequestEvent;
import com.pickple.delivery.presentation.request.DeliveryStartRequest;
import java.util.List;
import java.util.UUID;

public class DeliveryMapper {

    public static DeliveryCreateRequestDto convertCreateRequestEventToDto(
            DeliveryCreateRequestEvent event) {
        return DeliveryCreateRequestDto.builder()
                .orderId(event.getOrderId())
                .deliveryRequirement(event.getDeliveryRequirement())
                .recipientName(event.getRecipientName())
                .recipientAddress(event.getRecipientAddress())
                .recipientContact(event.getRecipientContact())
                .build();
    }

    public static DeliveryStartRequestDto convertStartRequestToDto(UUID deliveryId,
            DeliveryStartRequest dto) {
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

    public static DeliveryInfoDto convertProjectionToDto(DeliveryInfoProjection projection) {
        return DeliveryInfoDto.builder()
                .deliveryId(projection.getDeliveryId())
                .orderId(projection.getOrderId())
                .deliveryType(projection.getDeliveryType())
                .carrierName(projection.getCarrierName())
                .deliveryRequirement(projection.getDeliveryRequirement())
                .deliveryStatus(projection.getDeliveryStatus())
                .trackingNumber(projection.getTrackingNumber())
                .recipientName(projection.getRecipientName())
                .recipientContact(projection.getRecipientContact())
                .recipientAddress(projection.getRecipientAddress())
                .build();
    }

    public static DeliveryInfoResponseDto createDeliveryInfoResponseDto(DeliveryInfoDto infoDto,
            List<DeliveryDetailInfoDto> detailInfoDtoList) {
        return DeliveryInfoResponseDto.builder()
                .deliveryId(infoDto.getDeliveryId())
                .orderId(infoDto.getOrderId())
                .carrierName(infoDto.getCarrierName())
                .deliveryStatus(infoDto.getDeliveryStatus().getStatus())
                .deliveryType(infoDto.getDeliveryType().getTypeName())
                .deliveryRequirement(infoDto.getDeliveryRequirement())
                .recipientAddress(infoDto.getRecipientAddress())
                .recipientContact(infoDto.getRecipientContact())
                .recipientName(infoDto.getRecipientName())
                .trackingNumber(infoDto.getTrackingNumber())
                .deliveryDetailList(detailInfoDtoList)
                .build();
    }
}