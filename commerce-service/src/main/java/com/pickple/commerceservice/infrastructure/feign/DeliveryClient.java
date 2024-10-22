package com.pickple.commerceservice.infrastructure.feign;

import com.pickple.commerceservice.infrastructure.feign.dto.DeliveryClientDto;
import com.pickple.commerceservice.infrastructure.feign.fallback.DeliveryClientFallback;
import com.pickple.common_module.presentation.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@Primary
@FeignClient(name = "delivery-service", fallback = DeliveryClientFallback.class) // 자동으로 구현체를 만들어줌
public interface DeliveryClient {

    @GetMapping("/api/v1/deliveries/orders/{orderId}")
    ApiResponse<DeliveryClientDto> getDeliveryInfo(
            @RequestHeader("X-User-Roles") String authority,
            @RequestHeader("X-User-Name") String username,
            @PathVariable("orderId") UUID orderId);
}