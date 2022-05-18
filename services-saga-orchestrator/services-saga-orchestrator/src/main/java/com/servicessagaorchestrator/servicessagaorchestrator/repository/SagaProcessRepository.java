package com.servicessagaorchestrator.servicessagaorchestrator.repository;

import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sergey B.
 * 18.05.2022
 */
@Repository
public interface SagaProcessRepository extends JpaRepository<SagaProcess, Long> {
}
