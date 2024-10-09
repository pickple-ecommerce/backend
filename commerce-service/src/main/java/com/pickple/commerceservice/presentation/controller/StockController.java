package com.pickple.commerceservice.presentation.controller;

import com.pickple.commerceservice.application.dto.StockByProductDto;
import com.pickple.commerceservice.application.dto.StockResponseDto;
import com.pickple.commerceservice.application.service.StockService;
import com.pickple.common_module.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    /**
     * 상품 별 재고 조회
     */
    @GetMapping("/products/{productId}")
    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<StockByProductDto>> getStockByProductId(@PathVariable UUID productId) {
        StockByProductDto stock = stockService.getStockByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "상품 별 재고 조회 성공", stock));
    }
}
