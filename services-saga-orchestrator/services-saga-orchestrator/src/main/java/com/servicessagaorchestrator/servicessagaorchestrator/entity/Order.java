package com.servicessagaorchestrator.servicessagaorchestrator.entity;

import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
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
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Getter
@Setter
@Entity
@Table(name = "\"Order\"")
public class Order extends AbstractEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

}
