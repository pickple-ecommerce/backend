package com.pickple.delivery.infrastructure.feign;

import com.pickple.delivery.application.port.OrderClient;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "commerce-service")
public interface OrderFeignClient extends OrderClient {

    @GetMapping("/api/v1/orders/deliveries/{deliveryId}/username")
    String getUsernameByDeliveryId(
            @PathVariable("deliveryId") UUID deliveryId,
            @RequestHeader("X-User-Roles") String role,
            @RequestHeader("X-User-Name") String username
    );
}
