package com.pickple.commerceservice.infrastructure.feign.fallback;

import com.pickple.commerceservice.infrastructure.feign.DeliveryClient;
import com.pickple.commerceservice.infrastructure.feign.dto.DeliveryClientDto;
import com.pickple.common_module.presentation.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeliveryClientFallback implements DeliveryClient {

    @Override
    public ApiResponse<DeliveryClientDto> getDeliveryInfo(String authority, String username, UUID orderId) {
        return new ApiResponse<>(HttpStatus.SERVICE_UNAVAILABLE, "배송 정보를 가져올 수 없습니다.", null);
    }
}
