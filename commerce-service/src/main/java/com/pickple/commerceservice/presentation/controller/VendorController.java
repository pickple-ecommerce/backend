package com.pickple.commerceservice.presentation.controller;

import com.pickple.commerceservice.application.service.VendorService;
import com.pickple.commerceservice.presentation.dto.request.VendorCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.VendorUpdateRequestDto;
import com.pickple.commerceservice.application.dto.VendorCreateResponseDto;
import com.pickple.commerceservice.application.dto.VendorResponseDto;
import com.pickple.common_module.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    /**
     * vendor 생성
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<VendorCreateResponseDto>> createVendor(
            @RequestBody VendorCreateRequestDto requestDto,
            @RequestHeader("X-User-Name") String username) {
        VendorCreateResponseDto responseDto = vendorService.createVendor(requestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, "업체가 성공적으로 생성되었습니다.", responseDto));
    }

    /**
     * vendor 전체 조회
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MASTER')")
    public ResponseEntity<ApiResponse<Page<VendorResponseDto>>> getAllVendors(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "전체 업체 목록입니다.", vendorService.getAllVendors(pageable)));
    }

    /**
     * vendor 단건 조회
     */
    @GetMapping("/{vendorId}")
    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<VendorResponseDto>> getVendorById(
            @PathVariable UUID vendorId) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "업체 상세 정보입니다.", vendorService.getVendorById(vendorId)));
    }

    /**
     * vendor 수정
     */
    @PutMapping("/{vendorId}")
    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<VendorResponseDto>> updateVendor(
            @PathVariable UUID vendorId,
            @RequestBody VendorUpdateRequestDto requestDto,
            @RequestHeader("X-User-Name") String username) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "업체가 성공적으로 수정되었습니다.", vendorService.updateVendor(vendorId, requestDto, username)));
    }

    /**
     * vendor 삭제
     */
    @DeleteMapping("/{vendorId}")
    @PreAuthorize("hasAnyAuthority('VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<VendorResponseDto>> deleteVendor(
            @PathVariable UUID vendorId,
            @RequestHeader("X-User-Name") String username) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "업체가 성공적으로 삭제되었습니다.", vendorService.deleteVendor(vendorId, username)));
    }

    /**
     * vendor 검색
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('USER', 'VENDOR_MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<Page<VendorResponseDto>>> searchVendors(
            @RequestParam(value = "keyword", required = false) String keyword,
            Pageable pageable) {
        Page<VendorResponseDto> response = vendorService.searchVendors(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "검색 결과입니다.", response));
    }
}
