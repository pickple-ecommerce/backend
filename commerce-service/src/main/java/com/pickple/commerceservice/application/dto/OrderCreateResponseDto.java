package com.pickple.commerceservice.application.dto;

import com.pickple.commerceservice.domain.model.Order;
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
public class OrderCreateResponseDto {

    private UUID orderId;
    private String username;
    private BigDecimal amount;
    private String orderStatus;
    private List<OrderDetailResponseDto> orderDetails;

    public static OrderCreateResponseDto fromEntity(Order order) {
        return OrderCreateResponseDto.builder()
                .orderId(order.getOrderId())
                .username(order.getUsername())
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus().name())
                .orderDetails(order.getOrderDetails().stream()
                        .map(OrderDetailResponseDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
