package com.pickple.delivery.application.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryStartRequestDto {

    private UUID deliveryId;

    private String carrierName;

    private String deliveryType;

    private String trackingNumber;

}
