package com.pickple.commerceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients(basePackages = "com.pickple.commerceservice.infrastructure.feign")
public class CommerceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceServiceApplication.class, args);
    }

}
