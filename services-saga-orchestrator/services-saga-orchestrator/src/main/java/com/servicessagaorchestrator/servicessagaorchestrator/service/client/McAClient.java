package com.servicessagaorchestrator.servicessagaorchestrator.service.client;

import com.servicessagaorchestrator.servicessagaorchestrator.config.McAClientConfig;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.TaskInfoDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.TaskStatusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author M.Bezmen
 */
@FeignClient(name = "mc-a-client", configuration = McAClientConfig.class, url = "${client.mca.host}")
public interface McAClient extends StartProcessClientService {

    @PostMapping("/tasks")
    TaskInfoDto createTask(@RequestBody String taskId);

    @GetMapping("/{taskId}/status")
    TaskStatusDto getTaskStatus(@PathVariable final String taskId);

    @PostMapping("/{taskId}/cancel")
    TaskStatusDto cancelTask(@PathVariable final String taskId);

    @PostMapping("/{taskId}/revert")
    TaskStatusDto revertTask(@PathVariable final String taskId);
}
