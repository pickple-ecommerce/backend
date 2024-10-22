package com.pickple.commerceservice.presentation.controller;

import com.pickple.commerceservice.application.dto.PreOrderResponseDto;
import com.pickple.commerceservice.application.service.PreOrderService;
import com.pickple.commerceservice.presentation.dto.request.PreOrderCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.PreOrderUpdateRequestDto;
import com.pickple.common_module.presentation.dto.ApiResponse;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/pre-orders")
public class PreOrderController {

    private final PreOrderService preOrderService;

    /**
     * 모든 예약 구매 가능 상품 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PreOrderResponseDto>>> getAllPreOrders(
            @PageableDefault(size = 10, sort = {"preOrderStartDate"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PreOrderResponseDto> preOrders = preOrderService.getAllPreOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "모든 예약 구매 상품 목록 조회 성공", preOrders));
    }

    /**
     * 특정 예약 구매 정보 조회
     */
    @GetMapping("/{preOrderId}")
    public ResponseEntity<ApiResponse<PreOrderResponseDto>> getPreOrderById(@PathVariable UUID preOrderId) {
        PreOrderResponseDto preOrder = preOrderService.getPreOrderById(preOrderId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "특정 예약 구매 정보 조회 성공", preOrder));
    }

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
    public ResponseEntity<ApiResponse<PreOrderResponseDto>> getPreOrderByProductId(@PathVariable UUID productId) {
        PreOrderResponseDto preOrder = preOrderService.getPreOrderByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "특정 상품의 예약 구매 정보 조회 성공", preOrder));
    }

    /**
     * 특정 상품의 예약 구매 정보 수정
     */
    @PutMapping("/products/{productId}")
    @PreAuthorize("hasAuthority('MASTER')")
    public ResponseEntity<ApiResponse<PreOrderResponseDto>> updatePreOrderByProductId(@PathVariable UUID productId,
                                                                                      @RequestBody PreOrderUpdateRequestDto updateDto) {
        PreOrderResponseDto updatedPreOrder = preOrderService.updatePreOrderByProductId(productId, updateDto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "예약 구매 정보 수정 성공", updatedPreOrder));
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
