package com.pickple.delivery.application.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryDetailInfoDto {

    private Date deliveryDetailTime;

    private String deliveryDetailStatus;

    private String deliveryDetailDescription;

}
