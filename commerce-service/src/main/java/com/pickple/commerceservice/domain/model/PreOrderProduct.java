package com.pickple.commerceservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "p_preorder_products")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreOrderProduct extends Product {

    @Column(name = "pre_order_start_date", nullable = false)
    private LocalDateTime preOrderStartDate; // 예약 시작일

    @Column(name = "pre_order_end_date", nullable = false)
    private LocalDateTime preOrderEndDate; // 예약 종료일

    @Column(name = "pre_order_stock_quantity", nullable = false)
    private Long preOrderStockQuantity; // 예약 구매 가능한 재고 수량

}
