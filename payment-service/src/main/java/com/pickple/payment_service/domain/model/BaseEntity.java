package com.pickple.payment_service.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name="created_by", updatable = false, length = 100)
    private String createdBy;

    @LastModifiedDate
    @Column(name="updated_at", updatable = false)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name="updated_by", updatable = false, length = 100)
    private String updatedBy;

    @Column(name="deleted_at", updatable = false)
    private LocalDateTime deletedAt;

    @Column(name="deleted_by", length = 100)
    private String deletedBy;

    @Column(name="is_delete")
    protected Boolean isDelete;

    protected BaseEntity() {
        this.isDelete = false;
    }
}
