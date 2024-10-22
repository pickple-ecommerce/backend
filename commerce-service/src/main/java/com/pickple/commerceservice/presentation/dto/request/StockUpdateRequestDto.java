package com.pickple.commerceservice.presentation.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockUpdateRequestDto {

    @Min(value = 1, message = "재고 수량은 0보다 커야 합니다.")
    private Long stockQuantity;

}
