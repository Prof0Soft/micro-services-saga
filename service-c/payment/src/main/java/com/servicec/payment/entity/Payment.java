package com.servicec.payment.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author M.Bezmen
 */
@Getter
@Setter
@Entity
@Table(name="Payment")
public class Payment extends AbstractEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

}
