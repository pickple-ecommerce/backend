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
public class OrderDetailResponseDto {
    private UUID productId;
    private Long orderQuantity;
    private BigDecimal totalPrice;

    public static OrderDetailResponseDto fromEntity(OrderDetail orderDetail) {
        return OrderDetailResponseDto.builder()
                .productId(orderDetail.getProduct().getProductId())
                .orderQuantity(orderDetail.getOrderQuantity())
                .totalPrice(orderDetail.getTotalPrice())
                .build();
    }
}
