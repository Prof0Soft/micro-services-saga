package com.servicea.servicea.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author M.Bezmen
 */
@Getter
@Setter
@Entity
@Table(name="\"Order\"")
public class Order extends AbstractEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

}
