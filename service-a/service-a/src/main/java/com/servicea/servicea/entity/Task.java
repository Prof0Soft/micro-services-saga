package com.servicea.servicea.entity;

import com.servicea.servicea.type.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @PrePersist
    private void prePersist() {
        status = TaskStatus.CREATED;
    }
}

