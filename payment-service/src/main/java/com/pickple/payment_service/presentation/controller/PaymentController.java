package com.pickple.payment_service.presentation.controller;

import com.pickple.common_module.presentation.dto.CommonResponse;
import com.pickple.payment_service.application.dto.PaymentRespDto;
import com.pickple.payment_service.application.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

}