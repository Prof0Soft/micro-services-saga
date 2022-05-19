package com.servicea.order.repository;

import com.servicea.order.entity.Task;
import com.servicea.order.type.TaskStatus;
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
