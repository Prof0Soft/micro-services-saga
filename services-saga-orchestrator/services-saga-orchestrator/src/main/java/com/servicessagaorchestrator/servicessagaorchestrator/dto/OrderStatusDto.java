package com.servicessagaorchestrator.servicessagaorchestrator.dto;

import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
import lombok.Data;

/**
 * @author Sergey B.
 * 18.05.2022
 */
@Data
public class OrderStatusDto {
    private String id;
    private TaskStatus status;
}
