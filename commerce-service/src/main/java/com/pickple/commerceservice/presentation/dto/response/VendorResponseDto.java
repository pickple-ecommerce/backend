package com.pickple.commerceservice.presentation.dto.response;

import com.pickple.commerceservice.domain.model.Vendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorResponseDto {
    private UUID vendorId;
    private String vendorName;
    private String vendorAddress;
    private String username;

    public static VendorResponseDto fromEntity(Vendor vendor) {
        return VendorResponseDto.builder()
                .vendorId(vendor.getVendorId())
                .vendorName(vendor.getVendorName())
                .vendorAddress(vendor.getVendorAddress())
                .username(vendor.getUsername())
                .build();
    }
}