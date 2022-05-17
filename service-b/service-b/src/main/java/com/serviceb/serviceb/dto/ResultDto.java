package com.serviceb.serviceb.dto;

import com.serviceb.serviceb.type.TaskStatus;
import lombok.Data;

/**
 * @author M.Bezmen
 */
@Data
public class ResultDto {
    private String taskId;
    private TaskStatus status;
}
