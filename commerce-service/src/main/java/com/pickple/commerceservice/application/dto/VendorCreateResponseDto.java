package com.pickple.commerceservice.application.dto;

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
    private String username;
}
