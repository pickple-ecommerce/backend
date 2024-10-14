package com.pickple.commerceservice.application.dto;

import com.pickple.commerceservice.infrastructure.feign.dto.DeliveryClientDto;
import com.pickple.commerceservice.infrastructure.feign.dto.PaymentClientDto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class OrderResponseDto {

    private UUID orderId;
    private String username;
    private BigDecimal amount;
    private String orderStatus;
    private List<OrderDetailResponseDto> orderDetails;
    private PaymentClientDto paymentInfo;
    private DeliveryClientDto deliveryInfo;
}