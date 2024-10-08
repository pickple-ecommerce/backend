package com.pickple.commerceservice.presentation.controller;

import com.pickple.commerceservice.application.dto.ProductResponseDto;
import com.pickple.commerceservice.application.service.ProductService;
import com.pickple.commerceservice.presentation.dto.request.ProductCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.ProductUpdateRequestDto;
import com.pickple.common_module.presentation.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    /**
     * 상품 전체 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getAllProducts(
            @PageableDefault(size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductResponseDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "상품 조회 성공", products));
    }

    /**
     * 상품 상세 조회
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable UUID productId) {
        ProductResponseDto product = productService.getProductById(productId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "상품 상세 조회 성공", product));
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(@PathVariable UUID productId, @RequestBody @Valid ProductUpdateRequestDto updateDto) {
        ProductResponseDto product = productService.updateProduct(productId, updateDto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "상품 수정 성공", product));
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable UUID productId) {
        productService.softDeleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "상품 삭제 성공", null));
    }

}
