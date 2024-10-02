package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.model.Vendor;
import com.pickple.commerceservice.domain.repository.VendorRepository;
import com.pickple.commerceservice.presentation.dto.request.VendorCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.response.VendorCreateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    /**
     * vendor 생성
     */
    @Transactional
    public VendorCreateResponseDto createVendor(VendorCreateRequestDto requestDto) {

        Vendor vendor = Vendor.builder()
                .vendorName(requestDto.getVendorName())
                .vendorAddress(requestDto.getVendorAddress())
//                .userId(requestDto.getUserId())
                .build();

        Vendor savedVendor = vendorRepository.save(vendor);

        return new VendorCreateResponseDto(
                savedVendor.getVendorId(),
                savedVendor.getVendorName(),
                savedVendor.getVendorAddress()
//                ,savedVendor.getUserId()
        );
    }

}
