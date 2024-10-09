package com.pickple.commerceservice.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductUpdateRequestDto {

    private String productName;
    private String description;
    private BigDecimal productPrice;
    private String productImage;
    private Boolean isPublic;

}
