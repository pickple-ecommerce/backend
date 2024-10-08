package com.pickple.delivery.application.mapper;

import com.pickple.delivery.application.dto.request.DeliveryCreateRequestDto;
import com.pickple.delivery.application.dto.DeliveryDetailInfoDto;
import com.pickple.delivery.application.dto.DeliveryInfoDto;
import com.pickple.delivery.application.dto.request.DeliveryUpdateRequestDto;
import com.pickple.delivery.application.dto.response.DeliveryInfoResponseDto;
import com.pickple.delivery.application.dto.request.DeliveryStartRequestDto;
import com.pickple.delivery.application.dto.response.DeliveryStartResponseDto;
import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.domain.model.enums.DeliveryCarrier;
import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import com.pickple.delivery.infrastructure.messaging.events.DeliveryCreateRequestEvent;
import com.pickple.delivery.presentation.request.DeliveryStartRequest;
import com.pickple.delivery.presentation.request.DeliveryUpdateRequest;
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
                .deliveryCarrier(DeliveryCarrier.getFromCarrierName(dto.getCarrierName()))
                .deliveryType(DeliveryType.getDeliveryType(dto.getDeliveryType()))
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

    public static DeliveryInfoDto convertEntityToInfoDto(Delivery entity) {
        return DeliveryInfoDto.builder()
                .deliveryId(entity.getDeliveryId())
                .orderId(entity.getOrderId())
                .deliveryType(entity.getDeliveryType())
                .carrierName(entity.getCarrierName())
                .deliveryRequirement(entity.getDeliveryRequirement())
                .deliveryStatus(entity.getDeliveryStatus())
                .trackingNumber(entity.getTrackingNumber())
                .recipientName(entity.getRecipientName())
                .recipientContact(entity.getRecipientContact())
                .recipientAddress(entity.getRecipientAddress())
                .build();
    }

    public static DeliveryInfoResponseDto createDeliveryInfoResponseDto(DeliveryInfoDto infoDto,
            List<DeliveryDetailInfoDto> detailInfoDtoList) {
        return DeliveryInfoResponseDto.builder()
                .deliveryId(infoDto.getDeliveryId())
                .orderId(infoDto.getOrderId())
                .carrierName(infoDto.getCarrierName())
                .deliveryStatus(infoDto.getDeliveryStatus().getStatus())
                .deliveryType(infoDto.getDeliveryType() != null ? infoDto.getDeliveryType().getTypeName() : null)
                .deliveryRequirement(infoDto.getDeliveryRequirement())
                .recipientAddress(infoDto.getRecipientAddress())
                .recipientContact(infoDto.getRecipientContact())
                .recipientName(infoDto.getRecipientName())
                .trackingNumber(infoDto.getTrackingNumber())
                .deliveryDetailList(detailInfoDtoList)
                .build();
    }

    public static DeliveryUpdateRequestDto convertUpdateRequestToDto(DeliveryUpdateRequest request) {
        return DeliveryUpdateRequestDto.builder()
                .carrierName(DeliveryCarrier.getFromCarrierName(request.getCarrierName()))
                .recipientAddress(request.getRecipientAddress())
                .deliveryRequirement(request.getDeliveryRequirement())
                .deliveryStatus(DeliveryStatus.getFromStatus(request.getDeliveryStatus()))
                .deliveryType(DeliveryType.getDeliveryType(request.getDeliveryType()))
                .recipientContact(request.getRecipientContact())
                .recipientName(request.getRecipientName())
                .trackingNumber(request.getTrackingNumber())
                .build();
    }
}