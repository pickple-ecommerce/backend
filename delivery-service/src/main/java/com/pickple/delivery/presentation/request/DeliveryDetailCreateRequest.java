package com.pickple.delivery.presentation.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Getter;

@Getter
public class DeliveryDetailCreateRequest {

    @NotNull
    private Instant deliveryDetailTime;

    @NotNull
    private String deliveryDetailStatus;

    private String deliveryDetailDescription;

}
