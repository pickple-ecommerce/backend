package com.pickple.delivery.application.dto;

import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryInfoDto {

    private UUID deliveryId;

    private UUID orderId;

    private String carrierName;

    private DeliveryType deliveryType;

    private String trackingNumber;

    private DeliveryStatus deliveryStatus;

    private String deliveryRequirement;

    private String recipientName;

    private String recipientAddress;

    private String recipientContact;

}
