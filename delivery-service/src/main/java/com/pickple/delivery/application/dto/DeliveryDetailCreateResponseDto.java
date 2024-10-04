package com.pickple.delivery.application.dto;

import com.pickple.delivery.domain.model.DeliveryDetailId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryDetailCreateResponseDto {

    private DeliveryDetailId deliveryId;

    private String deliveryDetailStatus;

    private String deliveryDetailDescription;
}