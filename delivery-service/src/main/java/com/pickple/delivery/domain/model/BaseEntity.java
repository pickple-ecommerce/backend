package com.pickple.delivery.domain.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public abstract class BaseEntity implements Serializable {

    @Field("created_at")
    @CreatedDate
    private Date createdAt;

    @Field("updated_at")
    @LastModifiedDate
    private Date updatedAt;

    @Field("created_by")
    @CreatedBy
    private String createdBy;

    @Field("updated_by")
    @LastModifiedBy
    private String updatedBy;

}