package com.pickple.commerceservice.domain.model;

import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.common_module.domain.model.BaseEntity;
import com.pickple.common_module.exception.CustomException;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_stocks")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "stock_id")
    private UUID stockId;

    @Column(name = "stock_quantity", nullable = false)
    private Long stockQuantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Version
    @Column(name = "version")
    private Long version = 0L;

    public void updateStockQuantity(Long newQuantity) {
        if (newQuantity == null || newQuantity < 0) {
            throw new CustomException(CommerceErrorCode.STOCK_UNDER_ZERO);
        }
        this.stockQuantity = newQuantity;
    }

    // 재고 수량 1 증가 메서드
    public void increaseStock() {
        this.stockQuantity += 1;
    }

    // 재고 수량 1 감소 메서드
    public void decreaseStock() {
        if (this.stockQuantity <= 0) {
            throw new CustomException(CommerceErrorCode.INSUFFICIENT_STOCK);
        }
        this.stockQuantity -= 1;
    }

    // 주문한 수량 만큼 재고 감소 메서드
    public void decreaseStockQuantity(Long newQuantity) {
        this.stockQuantity = newQuantity;
    }

}
