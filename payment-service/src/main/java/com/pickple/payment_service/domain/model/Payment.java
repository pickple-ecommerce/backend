package com.pickple.payment_service.domain.model;

import com.pickple.common_module.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="p_payments")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="payment_id")
    private UUID paymentId;

    @Column(name="order_id")
    private UUID orderId;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="method")
    private String method;

    @Column(name="status")
    @Enumerated(value=EnumType.STRING)
    private PaymentStatusEnum status;

    @Column(name="approval_number")
    private String approvalNumber;

    public Payment(UUID orderId, BigDecimal amount) {
        this.orderId = orderId;
        this.amount = amount;
        this.method = "CREDIT-CARD";
        this.status = PaymentStatusEnum.PENDING;
    }

    public void success() {
        this.status = PaymentStatusEnum.SUCCESS;
    }

    public void fail() {
        this.status = PaymentStatusEnum.FAILED;
    }
}
