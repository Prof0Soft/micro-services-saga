package com.servicessagaorchestrator.servicessagaorchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ServicesSagaOrchestratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicesSagaOrchestratorApplication.class, args);
    }

}
