package com.servicessagaorchestrator.servicessagaorchestrator;

import com.servicessagaorchestrator.servicessagaorchestrator.repository.OrderRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.SagaProcessRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
class ServicesSagaOrchestratorApplicationTests {
    @MockBean
    private SagaProcessRepository sagaProcessRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private AuditingHandler auditingHandler;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    void contextLoads() {

    }

}
