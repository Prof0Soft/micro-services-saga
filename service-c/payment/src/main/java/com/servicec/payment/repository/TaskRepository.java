package com.servicec.payment.repository;

import com.servicec.payment.entity.Task;
import com.servicec.payment.type.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByStatus(TaskStatus status);
}
