package com.pickple.delivery.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeliveryStartRequest {

    @NotBlank
    private String carrierName;

    @NotBlank
    private String deliveryType;

    @NotBlank
    private String trackingNumber;

}
