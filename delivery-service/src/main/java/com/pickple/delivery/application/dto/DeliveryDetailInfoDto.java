package com.pickple.delivery.application.dto;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryDetailInfoDto {

    private Instant deliveryDetailTime;

    private String deliveryDetailStatus;

    private String deliveryDetailDescription;

}
