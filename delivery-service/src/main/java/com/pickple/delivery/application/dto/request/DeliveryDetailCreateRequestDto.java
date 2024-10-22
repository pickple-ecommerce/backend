package com.pickple.delivery.application.dto.request;

import java.util.Date;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryDetailCreateRequestDto {

    private UUID deliveryId;

    private Date deliveryDetailTime;

    private String deliveryDetailStatus;

    private String deliveryDetailDescription;
}
