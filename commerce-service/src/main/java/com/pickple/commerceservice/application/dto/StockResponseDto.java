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
public class StockResponseDto {

    private UUID stockId;
    private Long stockQuantity;

    public static StockResponseDto fromEntity(Stock stock) {
        return StockResponseDto.builder()
                .stockId(stock.getStockId())
                .stockQuantity(stock.getStockQuantity())
                .build();
    }
}
