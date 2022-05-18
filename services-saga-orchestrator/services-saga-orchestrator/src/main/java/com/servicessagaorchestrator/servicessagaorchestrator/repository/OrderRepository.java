package com.servicessagaorchestrator.servicessagaorchestrator.repository;

import com.servicessagaorchestrator.servicessagaorchestrator.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
}
