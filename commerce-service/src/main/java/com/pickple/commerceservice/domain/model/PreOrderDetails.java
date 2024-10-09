package com.pickple.commerceservice.domain.model;

import com.pickple.common_module.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_pre_order_details")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PreOrderDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID preOrderId;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "pre_order_start_date", nullable = false)
    private LocalDateTime preOrderStartDate; // 예약 시작일

    @Column(name = "pre_order_end_date", nullable = false)
    private LocalDateTime preOrderEndDate;  // 예약 종료일

    @Column(name = "pre_order_stock_quantity", nullable = false)
    private Long preOrderStockQuantity;     // 예약 구매 가능한 재고 수량

    public void markAsDeleted() {
        this.isDelete = true;
        this.deletedAt = LocalDateTime.now();
    }
}
