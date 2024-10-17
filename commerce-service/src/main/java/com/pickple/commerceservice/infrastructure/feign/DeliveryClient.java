package com.pickple.commerceservice.infrastructure.feign;

import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.feign.dto.DeliveryClientDto;
import com.pickple.common_module.exception.CustomException;
import com.pickple.common_module.presentation.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "delivery-service", url = "http://${feign.host.delivery}:19096")
public interface DeliveryClient {

    @GetMapping("/api/v1/deliveries/orders/{orderId}")
    ResponseEntity<ApiResponse<DeliveryClientDto>> getDeliveryInfo(
            @RequestHeader("X-User-Roles") String authority,
            @RequestHeader("X-User-Name") String username,
            @PathVariable("orderId") UUID orderId);

    // 배송 API 데이터 파싱
    default DeliveryClientDto fetchDeliveryInfo(String authority, String username, UUID orderId) {
        ResponseEntity<ApiResponse<DeliveryClientDto>> response = getDeliveryInfo(authority, username, orderId);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getData();
        } else {
            throw new CustomException(CommerceErrorCode.DELIVERY_INFO_NOT_FOUND);
        }
    }
}