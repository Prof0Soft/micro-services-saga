package com.serviceb.serviceb.repository;

import com.serviceb.serviceb.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
}
