package com.pickple.payment_service.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="p_payments")
public class Payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="payment_id")
    private UUID paymentId;

    @JoinColumn(name="order_id")
    private UUID orderId;

    @Column(name="amount")
    private double amount;

    @Column(name="method")
    private String method;

    @Column(name="status")
    @Enumerated(value=EnumType.STRING)
    private PaymentStatusEnum status;

    @Column(name="approval_number")
    private String approvalNumber;
}
