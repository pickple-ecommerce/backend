package com.pickple.delivery.application.dto.response;

import com.pickple.delivery.application.dto.DeliveryDetailInfoDto;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryInfoResponseDto {

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

    private List<DeliveryDetailInfoDto> deliveryDetailList;

}
