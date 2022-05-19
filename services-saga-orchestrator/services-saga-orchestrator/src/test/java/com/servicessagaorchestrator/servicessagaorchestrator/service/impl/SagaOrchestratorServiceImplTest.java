package com.servicessagaorchestrator.servicessagaorchestrator.service.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.ResultDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.TaskStatusDto;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Order;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Step;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.BookingFlow;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
import com.servicessagaorchestrator.servicessagaorchestrator.exception.BadRequestException;
import com.servicessagaorchestrator.servicessagaorchestrator.exception.NotFoundException;
import com.servicessagaorchestrator.servicessagaorchestrator.mockdata.ResultDtoMock;
import com.servicessagaorchestrator.servicessagaorchestrator.mockdata.SagaProcessMock;
import com.servicessagaorchestrator.servicessagaorchestrator.mockdata.TaskInfoMock;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.OrderRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.SagaProcessRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.service.SagaService;
import com.servicessagaorchestrator.servicessagaorchestrator.service.client.McAClient;
import com.servicessagaorchestrator.servicessagaorchestrator.service.client.McBClient;
import com.servicessagaorchestrator.servicessagaorchestrator.service.client.McCClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Sergey B.
 * 19.05.2022
 */
@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
class SagaOrchestratorServiceImplTest {

    @Autowired
    private SagaService sagaService;
    @MockBean
    private McAClient mcAClient;
    @MockBean
    private McBClient mcBClient;
    @MockBean
    private McCClient mcCClient;
    @MockBean
    private SagaProcessRepository sagaProcessRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private AuditingHandler auditingHandler;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    void initSaga_successResult_nextStepIsPresent() {
        //GIVEN
        when(mcAClient.createTask(ResultDtoMock.create())).thenReturn(TaskInfoMock.create());

        //WHEN
        sagaService.initSaga(SagaProcessMock.create());

        //THEN
        verify(sagaProcessRepository).save(any());
        verify(orderRepository).save(any());
    }

    @Test
    void initSaga_successResult_nextStepIsNotPresent() {
        //GIVEN
        when(mcAClient.createTask(ResultDtoMock.create())).thenReturn(TaskInfoMock.create());

        //WHEN
        sagaService.initSaga(SagaProcessMock.create(TaskStatus.RUNNING));

        //THEN
        verify(sagaProcessRepository).save(any());
        verify(orderRepository).save(any());
    }

    @Test
    void initSaga_successResult_catchException() {
        //GIVEN
        final SagaProcess sagaProcess = SagaProcessMock.create();
        sagaProcess.setOrder(null);

        //THEN
        assertThrows(NullPointerException.class,
                () -> {
                    //WHEN
                    sagaService.initSaga(sagaProcess);
                });
    }

    @Test
    void nextSagaStep_revertedStatusSuccess() {
        //GIVEN
        final Logger logger = (Logger) LoggerFactory.getLogger(SagaOrchestratorServiceImpl.class);
        final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        logger.addAppender(listAppender);

        final ResultDto resultDto = ResultDtoMock.create();
        resultDto.setStatus(TaskStatus.REVERTED);

        //WHEN
        sagaService.nextSagaStep(resultDto);

        final List<ILoggingEvent> logsList = listAppender.list;

        //THEN
        assertEquals("Service: {} -> Task with id {} has been reverted.", logsList.get(0).getMessage());
        assertEquals(Level.INFO, logsList.get(0).getLevel());
    }

    @Test
    void nextSagaStep_doneStatusSuccess() {
        //GIVEN
        final SagaProcess sagaProcess = SagaProcessMock.createLastStepFlow();

        when(sagaProcessRepository.findByOrderId(Mockito.any())).thenReturn(sagaProcess);

        final ResultDto resultDto = ResultDtoMock.create();
        resultDto.setStatus(TaskStatus.DONE);

        //WHEN
        sagaService.nextSagaStep(resultDto);

        //THEN
        verify(sagaProcessRepository, times(2)).save(any());
    }

    @Test
    void nextSagaStep_failedStatusSuccess() {
        //GIVEN
        final SagaProcess sagaProcess = SagaProcessMock.createLastStepFlow();
        final UUID expectedId = sagaProcess.getOrder().getId();

        when(sagaProcessRepository.findByOrderId(Mockito.any())).thenReturn(sagaProcess);

        final ResultDto resultDto = ResultDtoMock.create();

        resultDto.setStatus(TaskStatus.FAILED);

        //WHEN
        sagaService.nextSagaStep(resultDto);

        //THEN
        verify(mcAClient).revertTask(expectedId);
        verify(mcBClient).revertTask(expectedId);
    }

    @Test
    void cancelSaga_Success() {
        //GIVEN
        String taskId = "23e4567-e89b-12d3-a456-426614174000";
        UUID id = UUID.fromString(taskId);

        List<Step> steps = BookingFlow.buildFlow();
        steps.get(0).setId(1L);
        steps.get(1).setId(2L);
        steps.get(2).setId(3L);

        Order order = new Order();
        order.setId(id);
        order.setStatus(TaskStatus.RUNNING);
        SagaProcess flow = new SagaProcess();
        flow.setSteps(steps);
        flow.setActiveStepId(2L);
        flow.setId(1L);
        flow.setOrder(order);

        when(sagaProcessRepository.findByOrderId(UUID.fromString(taskId))).thenReturn(flow);
        when(mcBClient.cancelTask(id)).thenReturn(new TaskStatusDto());
        sagaService.cancelSaga(id);

        verify(sagaProcessRepository, times(2)).save(any());
        verify(mcBClient).cancelTask(id);
        verify(mcAClient).revertTask(id);

    }

    @Test
    void cancelSaga_NotFound() {
        String taskId = "23e4567-e89b-12d3-a456-426614174000";
        UUID id = UUID.fromString(taskId);

        when(sagaProcessRepository.findByOrderId(UUID.fromString(taskId))).thenReturn(null);
        assertThrows(NotFoundException.class, () -> sagaService.cancelSaga(id));
    }

    @Test
    void cancelSaga_BadRequest() {
        String taskId = "23e4567-e89b-12d3-a456-426614174000";
        UUID id = UUID.fromString(taskId);

        Order order = new Order();
        order.setId(id);
        order.setStatus(TaskStatus.DONE);
        SagaProcess flow = new SagaProcess();
        flow.setActiveStepId(2L);
        flow.setId(1L);
        flow.setOrder(order);

        when(sagaProcessRepository.findByOrderId(UUID.fromString(taskId))).thenReturn(flow);

        assertThrows(BadRequestException.class, () -> sagaService.cancelSaga(id));
    }

}