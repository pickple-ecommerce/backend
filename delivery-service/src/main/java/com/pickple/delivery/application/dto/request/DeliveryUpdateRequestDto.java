package com.pickple.delivery.application.dto.request;

import com.pickple.delivery.domain.model.enums.DeliveryCarrier;
import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryUpdateRequestDto {

    private DeliveryCarrier carrierName;

    private DeliveryType deliveryType;

    private String trackingNumber;

    private DeliveryStatus deliveryStatus;

    private String deliveryRequirement;

    private String recipientName;

    private String recipientAddress;

    private String recipientContact;
}