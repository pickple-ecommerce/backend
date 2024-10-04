package com.pickple.delivery.application.dto.response;

import com.pickple.delivery.domain.model.DeliveryDetailId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryDetailCreateResponseDto {

    private DeliveryDetailId deliveryDetailId;

    private String deliveryDetailStatus;

    private String deliveryDetailDescription;
}