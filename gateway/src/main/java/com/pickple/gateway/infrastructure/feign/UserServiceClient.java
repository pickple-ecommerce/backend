package com.pickple.gateway.infrastructure.feign;

import com.pickple.gateway.application.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/api/v1/users/{username}")
    UserDto getUserByUsername(@PathVariable("username") String username);
}
