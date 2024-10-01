package com.pickple.delivery.domain.service;

import com.pickple.delivery.application.dto.DeliveryCreateRequestDto;
import com.pickple.delivery.domain.model.Delivery;
import org.springframework.stereotype.Service;

@Service
public class DeliveryDomainService {

    public Delivery createDelivery(DeliveryCreateRequestDto dto) {
        return Delivery.builder()
                .orderId(dto.getOrderId())
                .deliveryRequirement(dto.getDeliveryRequirement())
                .recipientName(dto.getRecipientName())
                .recipientAddress(dto.getRecipientAddress())
                .recipientContact(dto.getRecipientContact())
                .build();
    }
}
