package com.servicea.order.mapper;

import com.servicea.order.dto.ResultDto;
import com.servicea.order.dto.TaskInfoDto;
import com.servicea.order.dto.TaskStatusDto;
import com.servicea.order.entity.Task;
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
