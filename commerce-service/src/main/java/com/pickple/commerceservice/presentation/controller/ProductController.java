package com.pickple.commerceservice.presentation.controller;

import com.pickple.commerceservice.application.dto.ProductResponseDto;
import com.pickple.commerceservice.application.service.ProductService;
import com.pickple.commerceservice.presentation.dto.request.ProductCreateRequestDto;
import com.pickple.common_module.presentation.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 생성
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(@RequestBody @Valid ProductCreateRequestDto createDto) {
        ProductResponseDto product = productService.createProduct(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED, "상품 생성 성공", product));
    }

}
