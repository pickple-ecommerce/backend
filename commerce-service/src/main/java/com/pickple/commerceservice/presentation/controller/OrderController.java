package com.pickple.commerceservice.presentation.controller;

import com.pickple.commerceservice.application.service.OrderService;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.response.OrderCreateResponseDto;
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
     * 주문 생성
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

    @PostMapping("/{orderId}/payment-complete")
    @PreAuthorize("hasAnyAuthority('USER', 'MASTER')")
    public ResponseEntity<ApiResponse<Void>> paymentComplete(
            @PathVariable UUID orderId,
            @RequestHeader("X-User-Name") String username) {
        orderService.handlePaymentComplete(orderId, username);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, "결제 완료 후 배송 요청이 전송되었습니다.", null));
    }

}
