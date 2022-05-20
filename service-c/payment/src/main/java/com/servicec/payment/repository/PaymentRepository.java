package com.servicec.payment.repository;

import com.servicec.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Modifying
    boolean removeByTaskId(UUID taskId);
}
