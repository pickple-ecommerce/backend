package com.pickple.commerceservice.application.dto;

import com.pickple.commerceservice.domain.model.OrderDetail;
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
public class OrderByVendorResponseDto {
    private UUID orderId;
    private String username;
    private BigDecimal amount;
    private String orderStatus;
    private UUID productId;
    private Long orderQuantity;

    public static OrderByVendorResponseDto fromEntity(OrderDetail orderDetail) {
        return OrderByVendorResponseDto.builder()
                .orderId(orderDetail.getOrder().getOrderId())
                .username(orderDetail.getOrder().getUsername())
                .amount(orderDetail.getOrder().getAmount())
                .orderStatus(orderDetail.getOrder().getOrderStatus().name())
                .productId(orderDetail.getProduct().getProductId())
                .orderQuantity(orderDetail.getOrderQuantity())
                .build();
    }
}
