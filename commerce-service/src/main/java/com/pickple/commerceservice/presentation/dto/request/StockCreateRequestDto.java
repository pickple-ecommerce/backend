package com.pickple.commerceservice.presentation.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockCreateRequestDto {

    @NotNull(message = "재고 수량은 필수입니다.")
    @Min(value = 1, message = "재고 수량은 0보다 커야 합니다.")
    private Long stockQuantity;

    @Setter
    private UUID productId;     // 연결된 상품 ID

}
