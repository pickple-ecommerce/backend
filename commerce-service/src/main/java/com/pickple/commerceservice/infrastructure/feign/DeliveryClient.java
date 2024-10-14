package com.pickple.commerceservice.infrastructure.feign;

import com.pickple.commerceservice.infrastructure.feign.dto.DeliveryClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "delivery-service", url = "http://localhost:19096")
public interface DeliveryClient {

    @GetMapping("/api/v1/deliveries/getDeliveryInfo/{order_id}")
    DeliveryClientDto getDeliveryInfo(
            @RequestHeader("X-User-Roles") String authority,
            @RequestHeader("X-User-Name") String username,
            @PathVariable("order_id") UUID orderId);
}