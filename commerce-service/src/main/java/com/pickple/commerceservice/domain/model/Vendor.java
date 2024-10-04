package com.pickple.commerceservice.domain.model;

import com.pickple.common_module.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_vendors")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "vendor_id", updatable = false, nullable = false)
    private UUID vendorId;

    @Column(name = "vendor_name", length = 255, nullable = false)
    private String vendorName;

    @Column(name = "vendor_address", length = 255, nullable = false)
    private String vendorAddress;

//    @Column(name = "user_id", nullable = false)
//    private Long userId;

    public Vendor updateVendor(String vendorName, String vendorAddress) {
        this.vendorName = vendorName;
        this.vendorAddress = vendorAddress;
        return this;
    }

    public Vendor softDelete() {
        this.isDelete = true;
        return this;
    }
}
