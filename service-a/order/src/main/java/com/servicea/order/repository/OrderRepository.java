package com.servicea.order.repository;

import com.servicea.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Modifying
    boolean removeByTaskId(UUID taskId);
}
