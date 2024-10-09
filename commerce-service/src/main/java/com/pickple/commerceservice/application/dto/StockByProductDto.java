package com.pickple.commerceservice.application.dto;

import com.pickple.commerceservice.domain.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockByProductDto {

    private UUID productId;
    private Long stockQuantity;

    public static StockByProductDto fromEntity(Stock stock) {
        return StockByProductDto.builder()
                .productId(stock.getProduct().getProductId())
                .stockQuantity(stock.getStockQuantity())
                .build();
    }
}
