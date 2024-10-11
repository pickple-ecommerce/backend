package com.pickple.commerceservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class VendorUpdateResponseDto {
    private UUID vendorId;
    private String vendorName;
    private String vendorAddress;
//    private Long userId;
}