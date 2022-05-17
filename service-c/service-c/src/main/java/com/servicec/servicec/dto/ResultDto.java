package com.servicec.servicec.dto;

import com.servicec.servicec.type.TaskStatus;
import lombok.Data;

/**
 * @author M.Bezmen
 */
@Data
public class ResultDto {
    private String taskId;
    private TaskStatus status;
}
