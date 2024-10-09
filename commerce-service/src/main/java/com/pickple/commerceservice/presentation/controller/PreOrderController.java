package com.pickple.commerceservice.presentation.controller;

import com.pickple.commerceservice.application.dto.PreOrderResponseDto;
import com.pickple.commerceservice.application.service.PreOrderService;
import com.pickple.commerceservice.presentation.dto.request.PreOrderCreateRequestDto;
import com.pickple.common_module.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pre-orders")
public class PreOrderController {

    private final PreOrderService preOrderService;

    /**
     * 특정 상품의 예약 구매 정보 등록
     */
    @PostMapping("/products/{productId}")
    @PreAuthorize("hasAuthority('MASTER')")
    public ResponseEntity<ApiResponse<PreOrderResponseDto>> createPreOrder(@PathVariable UUID productId, @RequestBody PreOrderCreateRequestDto requestDto) {
        PreOrderResponseDto preOrder = preOrderService.createPreOrder(productId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED, "특정 상품의 예약 구매 정보 등록 성공", preOrder));
    }

    /**
     * 특정 상품의 예약 구매 정보 조회
     */
    @GetMapping("/products/{productId}")
    @PreAuthorize("hasAuthority('MASTER')")
    public ResponseEntity<ApiResponse<PreOrderResponseDto>> getPreOrderByProductId(@PathVariable UUID productId) {
        PreOrderResponseDto preOrder = preOrderService.getPreOrderByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "특정 상품의 예약 구매 정보 조회 성공", preOrder));
    }

    /**
     * 특정 상품의 예약 구매 정보 삭제
     */
    @DeleteMapping("/products/{productId}")
    @PreAuthorize("hasAuthority('MASTER')")
    public ResponseEntity<ApiResponse<Void>> deletePreOrderByProductId(@PathVariable UUID productId) {
        preOrderService.deletePreOrderByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "예약 구매 정보 삭제 성공", null));
    }

}
