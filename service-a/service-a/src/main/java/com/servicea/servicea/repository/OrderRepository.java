package com.servicea.servicea.repository;

import com.servicea.servicea.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Modifying
    boolean removeOrderByAndTaskId(UUID taskId);
}
