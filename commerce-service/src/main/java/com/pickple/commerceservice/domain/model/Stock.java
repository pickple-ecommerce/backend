package com.pickple.commerceservice.domain.model;

import com.pickple.common_module.domain.model.BaseEntity;
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

}
