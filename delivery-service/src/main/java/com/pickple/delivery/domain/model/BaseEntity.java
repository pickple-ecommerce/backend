package com.pickple.delivery.domain.model;

import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.*;
import org.springframework.data.cassandra.core.mapping.Column;

@Getter
public abstract class BaseEntity implements Serializable {

    @Column("created_at")
    @CreatedDate
    private Instant createdAt;

    @Column("updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    @Column("created_by")
    @CreatedBy
    private String createdBy;

    @Column("updated_by")
    @LastModifiedBy
    private String updatedBy;

}