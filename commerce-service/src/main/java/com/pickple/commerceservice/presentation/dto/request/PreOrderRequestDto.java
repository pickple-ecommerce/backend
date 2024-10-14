package com.pickple.commerceservice.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PreOrderRequestDto {
    private UUID productId;
}
