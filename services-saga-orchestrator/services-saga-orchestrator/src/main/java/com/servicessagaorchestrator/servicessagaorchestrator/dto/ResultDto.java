package com.servicessagaorchestrator.servicessagaorchestrator.dto;

import com.servicessagaorchestrator.servicessagaorchestrator.type.TaskStatus;
import lombok.Data;

/**
 * @author M.Bezmen
 */
@Data
public class ResultDto {
    private String taskId;
    private TaskStatus status;
}
