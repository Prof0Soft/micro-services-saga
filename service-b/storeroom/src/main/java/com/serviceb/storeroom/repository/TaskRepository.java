package com.serviceb.storeroom.repository;

import com.serviceb.storeroom.entity.Task;
import com.serviceb.storeroom.type.TaskStatus;
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
