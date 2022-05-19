package com.servicec.payment.entity;

import com.servicec.payment.type.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Getter
@Setter
@Entity
public class Task extends AbstractEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "serviceName")
    private String serviceName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @PrePersist
    private void prePersist() {
        status = TaskStatus.CREATED;
    }
}

