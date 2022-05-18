package com.servicessagaorchestrator.servicessagaorchestrator.dto;

import com.servicessagaorchestrator.servicessagaorchestrator.type.TaskStatus;
import lombok.Data;

/**
 * @author M.Bezmen
 */
@Data
public class TaskStatusDto {
    private String id;
    private TaskStatus status;
}
