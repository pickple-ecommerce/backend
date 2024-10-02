package com.pickple.commerceservice.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VendorCreateRequestDto {
    private String vendorName;
    private String vendorAddress;
//    private Long userId;
}