package com.pickple.payment_service.presentation.controller;

import com.pickple.common_module.presentation.dto.ApiResponse;
import com.pickple.payment_service.application.dto.PaymentRespDto;
import com.pickple.payment_service.application.service.PaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("hasAuthority('MASTER')")
    @DeleteMapping("/{payment_id}")
    public ResponseEntity<Void> deletePayment(
            @PathVariable(name="payment_id") UUID paymentId
    ){
        paymentService.deletePayment(paymentId);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('USER', 'VENDOR_MANAGER', 'MASTER')")
    @GetMapping("{payment_id}")
    public ResponseEntity<ApiResponse<PaymentRespDto>> getPaymentDetails(
            @PathVariable(name="payment_id") UUID paymentId
    ) {

        ApiResponse<PaymentRespDto> response = ApiResponse.success(
                HttpStatus.OK,
                "해당 결제의 상세 내역이 조회되었습니다.",
                paymentService.getPaymentDetails(paymentId)
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('USER', 'VENDOR_MANAGER', 'MASTER')")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<Page<PaymentRespDto>>> getPaymentsHistory(
            @RequestHeader(name="X-User-Name") String userName,
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="size", defaultValue = "10") int size,
            @RequestParam(value="sort", defaultValue = "createdAt, desc") String[] sort
    ){
       Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

       ApiResponse<Page<PaymentRespDto>> response = ApiResponse.success(
               HttpStatus.OK,
               "전체 결제 내역이 조회 되었습니다.",
               paymentService.getPaymentsHistory(userName, pageable)
       );

       return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('MASTER')")
    @GetMapping("/getAllPayments")
    public ResponseEntity<ApiResponse<Page<PaymentRespDto>>> getAllPayments(
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="size", defaultValue = "10") int size,
            @RequestParam(value="sort", defaultValue = "createdAt, desc") String[] sort
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        ApiResponse<Page<PaymentRespDto>> response = ApiResponse.success(
                HttpStatus.OK,
                "admin > 전체 결제 내역이 조회 되었습니다.",
                paymentService.getAllPayments(pageable)
        );

        return ResponseEntity.ok(response);
    }
}
