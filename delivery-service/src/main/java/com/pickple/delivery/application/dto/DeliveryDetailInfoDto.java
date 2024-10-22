package com.pickple.delivery.application.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryDetailInfoDto implements Serializable {

    private Date deliveryDetailTime;

    private String deliveryDetailStatus;

    private String deliveryDetailDescription;

}
