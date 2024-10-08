package com.pickple.delivery.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeliveryUpdateRequest {

    @NotBlank
    private String carrierName;

    @NotBlank
    private String deliveryType;

    @NotBlank
    private String trackingNumber;

    @NotBlank
    private String deliveryStatus;

    @NotBlank
    private String deliveryRequirement;

    @NotBlank
    private String recipientName;

    @NotBlank
    private String recipientAddress;

    @NotBlank
    private String recipientContact;

}
