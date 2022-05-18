package com.servicessagaorchestrator.servicessagaorchestrator.dto;

import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
import lombok.Data;

/**
 * @author M.Bezmen
 */
@Data
public class ResultDto {
    private String taskId;
    private String serviceName;
    private TaskStatus status;
}
