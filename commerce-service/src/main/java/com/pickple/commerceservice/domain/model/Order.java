package com.pickple.commerceservice.domain.model;

import com.pickple.common_module.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID orderId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Builder.Default
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    // addOrderDetails 메서드
    public void addOrderDetails(List<OrderDetail> details) {
        this.orderDetails = details;
    }

    // 총 금액 계산
    public void calculateTotalAmount() {
        this.amount = orderDetails.stream()
                .map(OrderDetail::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 결제 ID 연결
    public void assignPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    // 주문 ID 연결
    public void assignDeliveryId(UUID deliveryId) {
        this.deliveryId = deliveryId;
    }

    // 주문 상태 변경
    public void changeStatus(OrderStatus newStatus) {
        this.orderStatus = newStatus;
    }

    // soft delete
    public void markAsDeleted() {
        this.isDelete = true;
    }
}
