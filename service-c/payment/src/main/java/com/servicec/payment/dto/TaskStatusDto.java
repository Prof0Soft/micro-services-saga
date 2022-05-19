package com.servicec.payment.dto;

import com.servicec.payment.type.TaskStatus;
import lombok.Data;

/**
 * @author M.Bezmen
 */
@Data
public class TaskStatusDto {
    private String id;
    private TaskStatus status;
}
