package com.pickple.commerceservice.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateResponseDto {

    private UUID orderId;
    private Long username;
    private BigDecimal amount;
    private String orderStatus;

    private List<OrderDetail> orderDetails;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetail {
        private UUID productId;
        private Long orderQuantity;
        private BigDecimal totalPrice;
    }
}
