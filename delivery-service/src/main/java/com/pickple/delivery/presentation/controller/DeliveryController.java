package com.pickple.delivery.presentation.controller;

import com.pickple.common_module.presentation.dto.ApiResponse;
import com.pickple.delivery.application.dto.DeliveryDetailCreateResponseDto;
import com.pickple.delivery.application.dto.DeliveryStartResponseDto;
import com.pickple.delivery.application.mapper.DeliveryDetailMapper;
import com.pickple.delivery.application.mapper.DeliveryMapper;
import com.pickple.delivery.application.service.DeliveryApplicationService;
import com.pickple.delivery.application.service.DeliveryDetailApplicationService;
import com.pickple.delivery.presentation.request.DeliveryDetailCreateRequest;
import com.pickple.delivery.presentation.request.DeliveryStartRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryApplicationService deliveryService;

    private final DeliveryDetailApplicationService deliveryDetailService;

    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    @PostMapping("/{delivery_id}/start")
    public ApiResponse<DeliveryStartResponseDto> startDelivery(
            @PathVariable("delivery_id") UUID deliveryId,
            @Valid @RequestBody DeliveryStartRequest request) {
        return ApiResponse.success(HttpStatus.OK, "배송이 등록되었습니다.", deliveryService.startDelivery(
                DeliveryMapper.convertStartRequestToDto(deliveryId, request)));
    }

    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    @PostMapping("/{delivery_id}/details")
    public ApiResponse<DeliveryDetailCreateResponseDto> createDeliveryDetail(
            @PathVariable("delivery_id") UUID deliveryId,
            @Valid @RequestBody DeliveryDetailCreateRequest request) {
        return ApiResponse.success(HttpStatus.OK, "배송 경로가 등록되었습니다.",
                deliveryDetailService.createDeliveryDetail(
                        DeliveryDetailMapper.convertCreateRequestToDto(deliveryId, request)));
    }

}