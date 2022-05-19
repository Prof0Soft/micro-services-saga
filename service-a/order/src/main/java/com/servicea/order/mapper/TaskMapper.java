package com.servicea.order.mapper;

import com.servicea.order.dto.ResultDto;
import com.servicea.order.dto.TaskInfoDto;
import com.servicea.order.dto.TaskStatusDto;
import com.servicea.order.entity.Task;
import org.mapstruct.Mapper;

/**
 * @author M.Bezmen
 */
@Mapper
public interface TaskMapper {

    ResultDto toResultDto(Task task);

    TaskInfoDto toInfoDto(Task task);
    TaskStatusDto toStatusDto(Task task);

}
