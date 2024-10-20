package com.pickple.notification_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="user-service")
public interface UserFeignClient {
    @GetMapping("/api/v1/users/get-user-email/{username}")
    String getUserEmail(@PathVariable("username") String username);
}
