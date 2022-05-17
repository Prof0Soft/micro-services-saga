package com.servicea.servicea.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

/**
 * @author M.Bezmen
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @Column(name = "created_on", updatable = false, nullable = false)
    @CreatedDate
    private Instant createdOn;

    @Column(name = "updated_on")
    @LastModifiedDate
    private Instant updatedOn;
}

