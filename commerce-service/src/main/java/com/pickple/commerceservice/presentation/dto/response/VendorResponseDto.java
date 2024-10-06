package com.pickple.commerceservice.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class VendorResponseDto {
    private UUID vendorId;
    private String vendorName;
    private String vendorAddress;
//    private Long userId;
}
