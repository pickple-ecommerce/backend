package com.pickple.delivery.application.dto.request;

import com.pickple.delivery.domain.model.enums.DeliveryCarrier;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryStartRequestDto {

    private UUID deliveryId;

    private DeliveryCarrier deliveryCarrier;

    private DeliveryType deliveryType;

    private String trackingNumber;

}
