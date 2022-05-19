package com.servicea.servicea.mapper;

import com.servicea.servicea.dto.ResultDto;
import com.servicea.servicea.dto.TaskInfoDto;
import com.servicea.servicea.dto.TaskStatusDto;
import com.servicea.servicea.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author M.Bezmen
 */
@Mapper
public interface TaskMapper {

    ResultDto toResultDto(Task task);

    TaskInfoDto toInfoDto(Task task);
    TaskStatusDto toStatusDto(Task task);

}
