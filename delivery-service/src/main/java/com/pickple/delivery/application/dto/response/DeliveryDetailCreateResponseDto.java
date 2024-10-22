package com.pickple.delivery.application.dto.response;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryDetailCreateResponseDto {

    private Date deliveryDetailTime;

    private String deliveryDetailStatus;

    private String deliveryDetailDescription;
}