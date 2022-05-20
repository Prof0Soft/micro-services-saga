package com.servicessagaorchestrator.servicessagaorchestrator.dto;

import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The status of order. Need for communicate and understand the statuses between orchestrator and services.
 *
 * @author Sergey B.
 * 18.05.2022
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderStatusDto {
    private String id;
    private TaskStatus status;
}
