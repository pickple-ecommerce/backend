package com.pickple.commerceservice.presentation.controller;

import com.pickple.commerceservice.application.dto.OrderByVendorResponseDto;
import com.pickple.commerceservice.application.dto.OrderCreateResponseDto;
import com.pickple.commerceservice.application.dto.OrderResponseDto;
import com.pickple.commerceservice.application.dto.OrderSummaryResponseDto;
import com.pickple.commerceservice.application.service.OrderService;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.PreOrderRequestDto;
import com.pickple.common_module.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
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
    @PreAuthorize("hasAnyAuthority('USER', 'VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(
            @PathVariable UUID orderId,
            @RequestHeader("X-User-Roles") String role,
            @RequestHeader("X-User-Name") String username) {

        OrderResponseDto orderResponse = orderService.cancelOrder(orderId, username, role);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "주문이 성공적으로 취소되었습니다.", orderResponse));
    }

    /**
     * 업체 별 주문 조회
     */
    @GetMapping("/{vendorId}/vendor")
    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<Page<OrderByVendorResponseDto>>> getOrdersByVendorId(
            @PathVariable UUID vendorId,
            @PageableDefault(size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<OrderByVendorResponseDto> orders = orderService.findByVendorId(vendorId, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "벤더별 주문 조회 성공", orders));
    }

    /**
     * 주문 전체 조회
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MASTER')")
    public ResponseEntity<ApiResponse<Page<OrderSummaryResponseDto>>> getAllOrders(
            @PageableDefault(size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<OrderSummaryResponseDto> orders = orderService.getAllOrders(pageable);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "주문 전체 조회 성공", orders));
    }

    /**
     * 내 주문 조회
     */
    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('USER', 'MASTER')")
    public ResponseEntity<ApiResponse<Page<OrderSummaryResponseDto>>> getMyOrders(
            @RequestHeader("X-User-Name") String username,
            @PageableDefault(size = 10, sort = {"createdAt", "updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<OrderSummaryResponseDto> orders = orderService.getOrdersByUsername(username, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "사용자의 주문 조회 성공", orders)
        );
    }
}
