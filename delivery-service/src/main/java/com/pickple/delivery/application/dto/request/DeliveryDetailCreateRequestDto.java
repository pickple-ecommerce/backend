package com.pickple.delivery.application.dto.request;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryDetailCreateRequestDto {

    private UUID deliveryId;

    private Instant deliveryDetailTime;

    private String deliveryDetailStatus;

    private String deliveryDetailDescription;
}
