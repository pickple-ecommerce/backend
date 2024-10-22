package com.pickple.payment_service.domain.model;

import com.pickple.common_module.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Column(name="username")
    private String username;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="method")
    private String method;

    @Column(name="status")
    @Enumerated(value=EnumType.STRING)
    private PaymentStatusEnum status;

    public Payment(UUID orderId, String userName, BigDecimal amount) {
        this.orderId = orderId;
        this.amount = amount;
        this.username = userName;
        this.method = "CREDIT-CARD";
        this.status = PaymentStatusEnum.PENDING;
    }

    public void success() {
        this.status = PaymentStatusEnum.COMPLETED;
    }

    public void cancel() {
        this.status = PaymentStatusEnum.CANCELED;
    }

    public void delete(String deleteBy) {
        this.isDelete = true;
        this.deletedBy = deleteBy;
        this.deletedAt = LocalDateTime.now();
    }
}
