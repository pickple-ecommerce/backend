package com.pickple.payment_service.domain.model;

import com.pickple.common_module.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
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

    @Column(name="user_id")
    private Long userId;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="method")
    private String method;

    @Column(name="status")
    @Enumerated(value=EnumType.STRING)
    private PaymentStatusEnum status;

    public Payment(UUID orderId, Long userId, BigDecimal amount) {
        this.orderId = orderId;
        this.amount = amount;
        this.userId = userId;
        this.method = "CREDIT-CARD";
        this.status = PaymentStatusEnum.PENDING;
    }

    public void success() {
        this.status = PaymentStatusEnum.COMPLETED;
    }

    public void canceled() {
        this.isDelete = true;
        this.status = PaymentStatusEnum.CANCELED;
    }
}
