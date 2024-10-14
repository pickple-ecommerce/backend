package com.pickple.commerceservice.application.dto;

import com.pickple.commerceservice.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductSearchResDto {

    private UUID productId;
    private String productName;
    private String description;
    private BigDecimal productPrice;
    private String productImage;

    public static ProductSearchResDto fromEntity(Product product) {
        return new ProductSearchResDto(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getProductPrice(),
                product.getProductImage()
        );
    }

}
