package com.pickple.commerceservice.application.dto;

import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.presentation.dto.response.VendorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    private UUID productId;
    private String productName;
    private String description;
    private BigDecimal productPrice;
    private String productImage;
    private Boolean isPublic;
    private VendorResponseDto vendor;
    private StockResponseDto stock;

    public static ProductResponseDto fromEntity(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .productPrice(product.getProductPrice())
                .productImage(product.getProductImage())
                .isPublic(product.getIsPublic())
                .vendor(VendorResponseDto.fromEntity(product.getVendor()))
                .stock(product.getStock() != null ? StockResponseDto.fromEntity(product.getStock()) : null)
                .build();
    }

}
