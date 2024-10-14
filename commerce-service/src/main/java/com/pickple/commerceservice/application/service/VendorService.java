package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.model.Vendor;
import com.pickple.commerceservice.domain.repository.VendorRepository;
import com.pickple.commerceservice.presentation.dto.request.VendorCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.VendorUpdateRequestDto;
import com.pickple.commerceservice.application.dto.VendorCreateResponseDto;
import com.pickple.commerceservice.application.dto.VendorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    /**
     * vendor 생성
     */
    @Transactional
    public VendorCreateResponseDto createVendor(VendorCreateRequestDto requestDto, String username) {
        Vendor vendor = Vendor.builder()
                .vendorName(requestDto.getVendorName())
                .vendorAddress(requestDto.getVendorAddress())
                .username(username)
                .build();

        Vendor savedVendor = vendorRepository.save(vendor);

        return new VendorCreateResponseDto(
                savedVendor.getVendorId(),
                savedVendor.getVendorName(),
                savedVendor.getVendorAddress(),
                savedVendor.getUsername()
        );
    }

    /**
     * vendor 전체 조회
     */
    @Transactional(readOnly = true)
    public Page<VendorResponseDto> getAllVendors(Pageable pageable) {
        return vendorRepository.findAllByIsDeleteFalse(pageable)
                .map(vendor -> new VendorResponseDto(
                        vendor.getVendorId(),
                        vendor.getVendorName(),
                        vendor.getVendorAddress(),
                        vendor.getUsername()
                ));
    }

    /**
     * vendor 단건 조회
     */
    @Transactional(readOnly = true)
    public VendorResponseDto getVendorById(UUID vendorId) {
        Vendor vendor = vendorRepository.findByVendorIdAndIsDeleteFalse(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        return new VendorResponseDto(
                vendor.getVendorId(),
                vendor.getVendorName(),
                vendor.getVendorAddress(),
                vendor.getUsername()
        );
    }

    /**
     * vendor 수정
     */
    @Transactional
    public VendorResponseDto updateVendor(UUID vendorId, VendorUpdateRequestDto requestDto, String username) {
        Vendor vendor = vendorRepository.findByVendorIdAndIsDeleteFalse(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.updateVendor(requestDto.getVendorName(), requestDto.getVendorAddress(), username);

        Vendor updatedVendor = vendorRepository.save(vendor);
        return new VendorResponseDto(
                updatedVendor.getVendorId(),
                updatedVendor.getVendorName(),
                updatedVendor.getVendorAddress(),
                updatedVendor.getUsername()
        );
    }

    /**
     * vendor 삭제
     */
    @Transactional
    public VendorResponseDto deleteVendor(UUID vendorId, String username) {
        Vendor vendor = vendorRepository.findByVendorIdAndIsDeleteFalse(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.softDelete();

        Vendor deletedVendor = vendorRepository.save(vendor);
        return new VendorResponseDto(
                deletedVendor.getVendorId(),
                deletedVendor.getVendorName(),
                deletedVendor.getVendorAddress(),
                deletedVendor.getUsername()
        );
    }

    /**
     * vendor 검색
     */
    @Transactional(readOnly = true)
    public Page<VendorResponseDto> searchVendors(String keyword, Pageable pageable) {
        return vendorRepository.searchVendors(keyword, pageable)
                .map(vendor -> new VendorResponseDto(
                        vendor.getVendorId(),
                        vendor.getVendorName(),
                        vendor.getVendorAddress(),
                        vendor.getUsername()
                ));
    }
}
