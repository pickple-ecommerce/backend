package com.pickple.payment_service.presentation.controller;

import com.pickple.payment_service.application.dto.PaymentCreateRespDto;
import com.pickple.payment_service.application.service.PaymentService;
import com.pickple.common_module.presentation.dto.CommonResponse;

import com.pickple.payment_service.presentation.request.PaymentCreateReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping()
    public CommonResponse<PaymentCreateRespDto> createPayment(@RequestBody PaymentCreateReqDto reqDto) {
        CommonResponse<PaymentCreateRespDto> response = CommonResponse.success(
                HttpStatus.OK,
                "test",
                paymentService.createPayment(reqDto)
        );
        return response;

    }




}
