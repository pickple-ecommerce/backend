package com.pickple.commerceservice.infrastructure.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class DeliveryClientDto {
    private UUID deliveryId;
    private UUID orderId;
    private String carrierName;
    private String deliveryType;
    private String trackingNumber;
    private String deliveryStatus;
    private String deliveryRequirement;
    private String recipientName;
    private String recipientAddress;
    private String recipientContact;
}
