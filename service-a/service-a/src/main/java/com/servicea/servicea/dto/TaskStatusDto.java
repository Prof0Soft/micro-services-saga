package com.servicea.servicea.dto;

import com.servicea.servicea.type.TaskStatus;
import lombok.Data;

/**
 * @author M.Bezmen
 */
@Data
public class TaskStatusDto {
    private String id;
    private TaskStatus status;
}
