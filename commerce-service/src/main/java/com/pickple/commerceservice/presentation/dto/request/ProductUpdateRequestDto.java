package com.pickple.commerceservice.presentation.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductUpdateRequestDto {

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String productName;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String description;

    @NotNull(message = "상품 가격은 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = false, message = "상품 가격은 0보다 커야합니다.")
    private BigDecimal productPrice;

    private String productImage;
    private Boolean isPublic;

}
