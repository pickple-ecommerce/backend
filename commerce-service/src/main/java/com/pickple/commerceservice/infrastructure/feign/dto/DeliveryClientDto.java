package com.pickple.commerceservice.infrastructure.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
