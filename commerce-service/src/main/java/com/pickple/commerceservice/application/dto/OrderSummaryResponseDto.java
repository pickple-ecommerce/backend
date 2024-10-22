package com.pickple.commerceservice.application.dto;

import com.pickple.commerceservice.domain.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryResponseDto {

    private UUID orderId;
    private String username;
    private BigDecimal amount;
    private String orderStatus;
    private UUID paymentId;
    private UUID deliveryId;

    public static OrderSummaryResponseDto fromEntity(Order order) {
        return OrderSummaryResponseDto.builder()
                .orderId(order.getOrderId())
                .username(order.getUsername())
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus().name())
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .build();
    }
}

