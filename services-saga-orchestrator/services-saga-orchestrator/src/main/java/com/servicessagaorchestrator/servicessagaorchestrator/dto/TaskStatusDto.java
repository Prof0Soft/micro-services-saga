package com.servicessagaorchestrator.servicessagaorchestrator.dto;

import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
import lombok.Data;

/**
 * The DTO use like response from services for understanding statuses of services.
 *
 * @author M.Bezmen
 */
@Data
public class TaskStatusDto {
    private String id;
    private TaskStatus status;
}
