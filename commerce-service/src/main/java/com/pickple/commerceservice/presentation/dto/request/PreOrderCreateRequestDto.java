package com.pickple.commerceservice.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PreOrderCreateRequestDto {

    private LocalDateTime preOrderStartDate;
    private LocalDateTime preOrderEndDate;
    private Long preOrderStockQuantity;

}
