package com.pickple.delivery.application.mapper;

import com.pickple.delivery.application.dto.DeliveryDetailCreateRequestDto;
import com.pickple.delivery.application.dto.DeliveryDetailCreateResponseDto;
import com.pickple.delivery.application.dto.DeliveryDetailInfoDto;
import com.pickple.delivery.domain.model.DeliveryDetail;
import com.pickple.delivery.domain.repository.DeliveryDetailInfoProjection;
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
                .deliveryDetailId(entity.getDeliveryDetailId())
                .deliveryDetailDescription(entity.getDeliveryDetailDescription())
                .deliveryDetailStatus(entity.getDeliveryDetailStatus())
                .build();
    }

    public static DeliveryDetailInfoDto convertProjectionToDto(
            DeliveryDetailInfoProjection projection) {
        return DeliveryDetailInfoDto.builder()
                .deliveryDetailTime(projection.getDeliveryDetailId().getDeliveryDetailTime())
                .deliveryDetailDescription(projection.getDeliveryDetailDescription())
                .deliveryDetailStatus(projection.getDeliveryDetailStatus())
                .build();
    }

}
