package com.pickple.commerceservice.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VendorCreateResponseDto {
    private UUID vendorId;
    private String vendorName;
    private String vendorAddress;
//    private Long userId;
}
