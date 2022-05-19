package com.servicea.order.dto;

import com.servicea.order.type.TaskStatus;
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
