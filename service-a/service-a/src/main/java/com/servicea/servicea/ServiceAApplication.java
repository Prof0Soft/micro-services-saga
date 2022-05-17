package com.servicea.servicea;

import com.servicea.servicea.dto.TaskInfoDto;
import com.servicea.servicea.service.impl.TaskServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
public class ServiceAApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(ServiceAApplication.class, args);
        TaskInfoDto task = run.getBean(TaskServiceImpl.class).createTask();
        System.out.println(task);
    }

}
