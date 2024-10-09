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

    public void updateStockQuantity(Long newQuantity) {
        if (newQuantity == null || newQuantity < 0) {
            throw new CustomException(CommerceErrorCode.STOCK_UNDER_ZERO);
        }
        this.stockQuantity = newQuantity;
    }

}
