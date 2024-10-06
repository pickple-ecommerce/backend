package com.pickple.delivery.application.dto.request;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryCreateRequestDto {

    private UUID orderId;

    private String deliveryRequirement;

    private String recipientName;

    private String recipientAddress;

    private String recipientContact;

}
