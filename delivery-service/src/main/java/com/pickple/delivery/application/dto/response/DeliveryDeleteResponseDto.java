package com.pickple.delivery.application.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryDeleteResponseDto {

    private UUID deliveryId;

    private UUID orderId;

    private String deletedBy;

}
