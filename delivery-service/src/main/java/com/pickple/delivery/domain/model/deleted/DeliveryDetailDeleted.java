package com.pickple.delivery.domain.model.deleted;

import com.pickple.delivery.domain.model.DeliveryDetail;
import com.pickple.delivery.domain.model.DeliveryDetailId;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("p_delivery_details_deleted")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryDetailDeleted {

    @PrimaryKey
    @Column("delivery_detail_id")
    private DeliveryDetailId deliveryDetailId;

    @Column("delivery_detail_status")
    private String deliveryDetailStatus;

    @Column("delivery_detail_description")
    private String deliveryDetailDescription;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_by")
    private String updatedBy;

    @Column("deleted_at")
    protected Instant deletedAt;

    @Column("deleted_by")
    protected String deletedBy;

    public void delete(String deleter) {
        this.deletedAt = Instant.now();
        this.deletedBy = deleter;
    }

    public static DeliveryDetailDeleted fromDeliveryDetail(DeliveryDetail deliveryDetail) {
        return DeliveryDetailDeleted.builder()
                .deliveryDetailId(deliveryDetail.getDeliveryDetailId())
                .deliveryDetailStatus(deliveryDetail.getDeliveryDetailStatus())
                .deliveryDetailDescription(deliveryDetail.getDeliveryDetailDescription())
                .createdAt(deliveryDetail.getCreatedAt())
                .createdBy(deliveryDetail.getCreatedBy())
                .updatedAt(deliveryDetail.getUpdatedAt())
                .updatedBy(deliveryDetail.getUpdatedBy())
                .build();
    }

}
