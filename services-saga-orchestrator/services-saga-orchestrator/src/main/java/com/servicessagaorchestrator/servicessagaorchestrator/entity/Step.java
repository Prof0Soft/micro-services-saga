package com.servicessagaorchestrator.servicessagaorchestrator.entity;

import com.servicessagaorchestrator.servicessagaorchestrator.enums.BookingFlow;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Sergey B.
 * 18.05.2022
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Step {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "service_name")
    @Enumerated(EnumType.STRING)
    private BookingFlow bookingFlow;

    @Column(name = "flow_order")
    private Integer flowOrder;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
}
