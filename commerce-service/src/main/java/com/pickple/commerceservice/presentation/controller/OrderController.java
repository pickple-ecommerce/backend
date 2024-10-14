package com.pickple.commerceservice.presentation.controller;

import com.pickple.commerceservice.application.dto.OrderCreateResponseDto;
import com.pickple.commerceservice.application.dto.OrderResponseDto;
import com.pickple.commerceservice.application.service.OrderService;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.PreOrderRequestDto;
import com.pickple.common_module.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 일반 주문 생성
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'MASTER')")
    public ResponseEntity<ApiResponse<OrderCreateResponseDto>> createOrder(
            @RequestBody OrderCreateRequestDto requestDto,
            @RequestHeader("X-User-Name") String username) {
        OrderCreateResponseDto responseDto = orderService.createOrder(requestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, "주문이 성공적으로 생성되었습니다.", responseDto));
    }

    /**
     * 예약 구매 주문 생성
     */
    @PostMapping("/pre-orders")
    public ResponseEntity<ApiResponse<Void>> createPreOrder(@RequestBody PreOrderRequestDto requestDto,
                                                            @RequestHeader("X-User-Name") String username) {
        orderService.createPreOrder(requestDto, username);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "예약 구매 주문이 성공적으로 생성되었습니다.", null));
    }

    /**
     * 주문 단건 조회
     */
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyAuthority('USER', 'MASTER')")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(
            @PathVariable UUID orderId,
            @RequestHeader("X-User-Roles") String role,
            @RequestHeader("X-User-Name") String username) {
        OrderResponseDto orderResponse = orderService.getOrderById(orderId, role, username);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "주문 조회 성공", orderResponse)
        );
    }

    /**
     * 주문 취소
     */
    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyAuthority('USER', 'MASTER')")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(
            @PathVariable UUID orderId,
            @RequestHeader("X-User-Roles") String role,
            @RequestHeader("X-User-Name") String username) {
        OrderResponseDto orderResponse = orderService.cancelOrder(orderId, username, role);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "주문이 성공적으로 취소되었습니다.", orderResponse));
    }
}
