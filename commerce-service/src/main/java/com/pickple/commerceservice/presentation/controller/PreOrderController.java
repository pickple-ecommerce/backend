package com.pickple.commerceservice.presentation.controller;

import com.pickple.commerceservice.application.service.PreOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pre-orders")
public class PreOrderController {

    private final PreOrderService preOrderService;

}
