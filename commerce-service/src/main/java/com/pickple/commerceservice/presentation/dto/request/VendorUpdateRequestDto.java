package com.pickple.commerceservice.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VendorUpdateRequestDto {
    private String vendorName;
    private String vendorAddress;
//    private Long userId;
}
