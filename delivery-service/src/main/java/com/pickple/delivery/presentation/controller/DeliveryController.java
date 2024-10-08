package com.pickple.delivery.presentation.controller;

import com.pickple.common_module.presentation.dto.ApiResponse;
import com.pickple.delivery.application.dto.response.DeliveryDeleteResponseDto;
import com.pickple.delivery.application.dto.response.DeliveryDetailCreateResponseDto;
import com.pickple.delivery.application.dto.response.DeliveryInfoResponseDto;
import com.pickple.delivery.application.dto.response.DeliveryStartResponseDto;
import com.pickple.delivery.application.mapper.DeliveryDetailMapper;
import com.pickple.delivery.application.mapper.DeliveryMapper;
import com.pickple.delivery.application.service.DeliveryApplicationService;
import com.pickple.delivery.application.service.DeliveryDetailApplicationService;
import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import com.pickple.delivery.presentation.request.DeliveryDetailCreateRequest;
import com.pickple.delivery.presentation.request.DeliveryStartRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryApplicationService deliveryService;

    private final DeliveryDetailApplicationService deliveryDetailService;

    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    @PostMapping("/{delivery_id}/start")
    public ResponseEntity<ApiResponse<DeliveryStartResponseDto>> startDelivery(
            @PathVariable("delivery_id") UUID deliveryId,
            @Valid @RequestBody DeliveryStartRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "배송이 등록되었습니다.", deliveryService.startDelivery(
                        DeliveryMapper.convertStartRequestToDto(deliveryId, request))));
    }

    @PreAuthorize("hasAnyAuthority('USER', 'VENDOR_MANAGER', 'MASTER')")
    @GetMapping("/{delivery_id}")
    public ResponseEntity<ApiResponse<DeliveryInfoResponseDto>> getDeliveryInfo(
            @PathVariable("delivery_id") UUID deliveryId) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "배송 조회에 성공하였습니다.",
                deliveryService.getDeliveryInfo(deliveryId)));
    }

    @PreAuthorize("hasAnyAuthority('MASTER')")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Delivery>>> getAllDeliveryInfo(
            @PageableDefault(
                    size = 10,
                    sort = {"createdAt", "updatedAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "배송 조회에 성공하였습니다.",
                deliveryService.getAllDelivery(pageable)));
    }

    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    @PostMapping("/{delivery_id}/details")
    public ResponseEntity<ApiResponse<DeliveryDetailCreateResponseDto>> createDeliveryDetail(
            @PathVariable("delivery_id") UUID deliveryId,
            @Valid @RequestBody DeliveryDetailCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "배송 경로가 등록되었습니다.",
                deliveryDetailService.createDeliveryDetail(
                        DeliveryDetailMapper.convertCreateRequestToDto(deliveryId, request))));
    }

    @PreAuthorize("hasAuthority('MASTER')")
    @DeleteMapping("/{delivery_id}")
    public ResponseEntity<ApiResponse<DeliveryDeleteResponseDto>> deleteDeliveryDetail(
            @PathVariable("delivery_id") UUID deliveryId) {
        String deleter = (String) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "성공적으로 삭제되었습니다.",
                deliveryService.deleteDelivery(deliveryId, deleter)));
    }

    // 배송사 기준 검색
    @PreAuthorize("hasAuthority('MASTER')")
    @GetMapping("/carrier")
    public ResponseEntity<ApiResponse<Page<Delivery>>> getDeliveriesByCarrier(
            @RequestParam String value,
            @PageableDefault(size = 10, sort = {"createdAt",
                    "updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Delivery> result = deliveryService.getDeliveriesByCarrier(value, pageable);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "배송사 기준 조회에 성공하였습니다.", result));
    }

    // 배송 상태 기준 검색
    @PreAuthorize("hasAuthority('MASTER')")
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Page<Delivery>>> getDeliveriesByStatus(
            @RequestParam DeliveryStatus value,
            @PageableDefault(size = 10, sort = {"createdAt",
                    "updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Delivery> result = deliveryService.getDeliveriesByStatus(value, pageable);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "배송 상태 기준 조회에 성공하였습니다.", result));
    }

    // 배송 유형 기준 검색
    @PreAuthorize("hasAuthority('MASTER')")
    @GetMapping("/type")
    public ResponseEntity<ApiResponse<Page<Delivery>>> getDeliveriesByDeliveryType(
            @RequestParam DeliveryType value,
            @PageableDefault(size = 10, sort = {"createdAt",
                    "updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Delivery> result = deliveryService.getDeliveriesByDeliveryType(value, pageable);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "배송 유형 기준 조회에 성공하였습니다.", result));
    }

    // 송장 번호 기준 검색
    @PreAuthorize("hasAuthority('MASTER')")
    @GetMapping("/tracking-number")
    public ResponseEntity<ApiResponse<DeliveryInfoResponseDto>> getDeliveriesByTrackingNumber(
            @RequestParam String value) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "송장번호 조회에 성공하였습니다.",
                deliveryService.getDeliveriesByTrackingNumber(value)));
    }

}