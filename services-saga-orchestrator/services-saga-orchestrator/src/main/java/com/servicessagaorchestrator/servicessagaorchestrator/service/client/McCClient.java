package com.servicessagaorchestrator.servicessagaorchestrator.service.client;

import com.servicessagaorchestrator.servicessagaorchestrator.config.McCClientConfig;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.TaskInfoDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.TaskStatusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author M.Bezmen
 */
@FeignClient(name = "mc-c-client", configuration = McCClientConfig.class, url = "${client.mcc.host}")
public interface McCClient extends StartProcessClientService {

    @PostMapping("/tasks")
    TaskInfoDto createTask();

    @GetMapping("/{taskId}/status")
    TaskStatusDto getTaskStatus(@PathVariable final String taskId);

    @PostMapping("/{taskId}/cancel")
    TaskStatusDto cancelTask(@PathVariable final String taskId);

    @PostMapping("/{taskId}/revert")
    TaskStatusDto revertTask(@PathVariable final String taskId);
}
