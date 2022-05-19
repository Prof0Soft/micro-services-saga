package com.serviceb.storeroom.mapper;

import com.serviceb.storeroom.dto.ResultDto;
import com.serviceb.storeroom.dto.TaskInfoDto;
import com.serviceb.storeroom.dto.TaskStatusDto;
import com.serviceb.storeroom.entity.Task;
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
