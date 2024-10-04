package com.pickple.delivery.application.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryStartResponseDto {

    private UUID deliveryId;

    private UUID orderId;

    private String carrierName;

    private String  deliveryType;

    private String trackingNumber;

    private String deliveryStatus;

    private String deliveryRequirement;

    private String recipientName;

    private String recipientAddress;

    private String recipientContact;
}
