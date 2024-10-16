package com.pickple.commerceservice.application.dto;

import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.infrastructure.feign.dto.DeliveryClientDto;
import com.pickple.commerceservice.infrastructure.feign.dto.PaymentClientDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private UUID orderId;
    private String username;
    private BigDecimal amount;
    private String orderStatus;
    private List<OrderDetailResponseDto> orderDetails;
    private PaymentClientDto paymentInfo;
    private DeliveryClientDto deliveryInfo;

    public static OrderResponseDto fromEntity(Order order, PaymentClientDto paymentInfo, DeliveryClientDto deliveryInfo) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .username(order.getUsername())
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus().name())
                .orderDetails(order.getOrderDetails().stream()
                        .map(OrderDetailResponseDto::fromEntity)
                        .collect(Collectors.toList()))
                .paymentInfo(paymentInfo)
                .deliveryInfo(deliveryInfo)
                .build();
    }
}