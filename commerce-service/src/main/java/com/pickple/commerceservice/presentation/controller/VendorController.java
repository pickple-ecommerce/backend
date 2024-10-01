package com.pickple.commerceservice.presentation.controller;

import com.pickple.commerceservice.application.service.VendorService;
import com.pickple.commerceservice.presentation.dto.request.VendorCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.response.VendorCreateResponseDto;
import com.pickple.common_module.presentation.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    /**
     * vendor 생성
     */
    @PostMapping
//    @PreAuthorize("hasAnyRole('VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<CommonResponse<VendorCreateResponseDto>> createVendor(@RequestBody VendorCreateRequestDto requestDto) {
        VendorCreateResponseDto responseDto = vendorService.createVendor(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(HttpStatus.CREATED, "업체가 성공적으로 생성되었습니다.", responseDto));
    }
}
