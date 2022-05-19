package com.servicessagaorchestrator.servicessagaorchestrator.dto;

import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
import lombok.Data;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Data
public class ResultDto {
    private UUID taskId;
    private String serviceName;
    private TaskStatus status;
}
