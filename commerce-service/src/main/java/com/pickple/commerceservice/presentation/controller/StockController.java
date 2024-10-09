package com.pickple.commerceservice.presentation.controller;

import com.pickple.commerceservice.application.dto.StockByProductDto;
import com.pickple.commerceservice.application.dto.StockResponseDto;
import com.pickple.commerceservice.application.service.StockService;
import com.pickple.commerceservice.presentation.dto.request.StockUpdateRequestDto;
import com.pickple.common_module.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 상품 별 재고 수량 수정
     */
    @PutMapping("/products/{productId}")
    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<StockByProductDto>> updateStockQuantity(@PathVariable UUID productId, @RequestBody StockUpdateRequestDto updateDto) {
        StockByProductDto stock = stockService.updateStockQuantity(productId, updateDto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "상품 재고 수정 성공", stock));
    }


}
