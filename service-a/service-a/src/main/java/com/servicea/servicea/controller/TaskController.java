package com.servicea.servicea.controller;

import com.servicea.servicea.dto.TaskInfoDto;
import com.servicea.servicea.dto.TaskStatusDto;
import com.servicea.servicea.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author M.Bezmen
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskInfoDto createTask(@RequestBody final String taskId, final String serviceName) {
        return taskService.createTask(taskId, serviceName);
    }

    @GetMapping("/{taskId}/status")
    public TaskStatusDto getTaskStatus(@PathVariable final String taskId) {
        return taskService.getTaskStatus(taskId);
    }

    @PostMapping("/{taskId}/cancel")
    public TaskStatusDto cancelTask(@PathVariable final String taskId) {
        return taskService.cancelTask(taskId);
    }

    @PostMapping("/{taskId}/revert")
    public TaskStatusDto revertTask(@PathVariable final String taskId) {
        return taskService.revertTask(taskId);
    }
}
