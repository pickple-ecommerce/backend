package com.pickple.commerceservice.application.dto;

import com.pickple.commerceservice.domain.model.PreOrderDetails;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PreOrderResponseDto {

    private UUID preOrderId;
    private UUID productId;
    private LocalDateTime preOrderStartDate;
    private LocalDateTime preOrderEndDate;
    private Long preOrderStockQuantity;

    public static PreOrderResponseDto fromEntity(PreOrderDetails preOrderDetails) {
        return PreOrderResponseDto.builder()
                .preOrderId(preOrderDetails.getPreOrderId())
                .productId(preOrderDetails.getProduct().getProductId())
                .preOrderStartDate(preOrderDetails.getPreOrderStartDate())
                .preOrderEndDate(preOrderDetails.getPreOrderEndDate())
                .preOrderStockQuantity(preOrderDetails.getPreOrderStockQuantity())
                .build();
    }

}
