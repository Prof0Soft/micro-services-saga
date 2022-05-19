package com.servicec.payment.dto;

import com.servicec.payment.type.TaskStatus;
import lombok.Data;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Data
public class TaskStatusDto {
    private UUID id;
    private TaskStatus status;
}
