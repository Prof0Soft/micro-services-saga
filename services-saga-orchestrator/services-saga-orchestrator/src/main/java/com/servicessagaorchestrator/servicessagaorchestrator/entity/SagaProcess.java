package com.servicessagaorchestrator.servicessagaorchestrator.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * @author Sergey B.
 * 18.05.2022
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class SagaProcess {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "active_step_id")
    private Long activeStepId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Step> steps;
}
