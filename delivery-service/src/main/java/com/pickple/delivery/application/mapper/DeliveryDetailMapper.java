package com.pickple.delivery.application.mapper;

import com.pickple.delivery.application.dto.DeliveryDetailCreateRequestDto;
import com.pickple.delivery.application.dto.DeliveryDetailCreateResponseDto;
import com.pickple.delivery.domain.model.DeliveryDetail;
import com.pickple.delivery.presentation.request.DeliveryDetailCreateRequest;
import java.util.UUID;

public class DeliveryDetailMapper {

    public static DeliveryDetailCreateRequestDto convertCreateRequestToDto(UUID deliveryId,
            DeliveryDetailCreateRequest request) {
        return DeliveryDetailCreateRequestDto.builder()
                .deliveryId(deliveryId)
                .deliveryDetailDescription(request.getDeliveryDetailDescription())
                .deliveryDetailTime(request.getDeliveryDetailTime())
                .deliveryDetailStatus(request.getDeliveryDetailStatus())
                .build();
    }

    public static DeliveryDetailCreateResponseDto convertEntityToCreateResponseDto(DeliveryDetail entity) {
        return DeliveryDetailCreateResponseDto.builder()
                .deliveryId(entity.getDeliveryDetailId())
                .deliveryDetailDescription(entity.getDeliveryDetailDescription())
                .deliveryDetailStatus(entity.getDeliveryDetailStatus())
                .build();
    }

}
