package com.servicec.payment.mapper;

import com.servicec.payment.dto.ResultDto;
import com.servicec.payment.dto.TaskInfoDto;
import com.servicec.payment.dto.TaskStatusDto;
import com.servicec.payment.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author M.Bezmen
 */
@Mapper
public interface TaskMapper {

    @Mapping(target = "taskId", source = "id")
    ResultDto toResultDto(Task task);

    TaskInfoDto toInfoDto(Task task);
    TaskStatusDto toStatusDto(Task task);

}
