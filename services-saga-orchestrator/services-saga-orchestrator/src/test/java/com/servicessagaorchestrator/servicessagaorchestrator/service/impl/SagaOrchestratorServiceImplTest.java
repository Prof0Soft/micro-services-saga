package com.servicessagaorchestrator.servicessagaorchestrator.service.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.ResultDto;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
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

    @Test
    void initSaga_successResult_nextStepIsPresent() {
        when(mcAClient.createTask(ResultDtoMock.create())).thenReturn(TaskInfoMock.create());

        sagaService.initSaga(SagaProcessMock.create());

        verify(sagaProcessRepository).save(any());
        verify(orderRepository).save(any());
    }

    @Test
    void initSaga_successResult_nextStepIsNotPresent() {
        when(mcAClient.createTask(ResultDtoMock.create())).thenReturn(TaskInfoMock.create());

        sagaService.initSaga(SagaProcessMock.create(TaskStatus.RUNNING));

        verify(sagaProcessRepository).save(any());
        verify(orderRepository).save(any());
    }

    @Test
    void initSaga_successResult_catchException() {
        final SagaProcess sagaProcess = SagaProcessMock.create();
        sagaProcess.setOrder(null);

        assertThrows(NullPointerException.class,
                () -> {
                    sagaService.initSaga(sagaProcess);
                });
    }

    @Test
    void nextSagaStep_revertedStatusSuccess() {
        final Logger logger = (Logger) LoggerFactory.getLogger(SagaOrchestratorServiceImpl.class);
        final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        logger.addAppender(listAppender);

        final ResultDto resultDto = ResultDtoMock.create();
        resultDto.setStatus(TaskStatus.REVERTED);
        sagaService.nextSagaStep(resultDto);

        final List<ILoggingEvent> logsList = listAppender.list;

        assertEquals("Service: {} -> Task with id {} has been reverted.", logsList.get(0)
                .getMessage());
        assertEquals(Level.INFO, logsList.get(0)
                .getLevel());
    }

    @Test
    void nextSagaStep_doneStatusSuccess() {
        final SagaProcess sagaProcess = SagaProcessMock.createLastStepFlow();

        when(sagaProcessRepository.findByOrderId(Mockito.any())).thenReturn(sagaProcess);

        final ResultDto resultDto = ResultDtoMock.create();
        resultDto.setStatus(TaskStatus.DONE);
        sagaService.nextSagaStep(resultDto);

        verify(sagaProcessRepository, times(2)).save(any());
    }

    @Test
    void nextSagaStep_failedStatusSuccess() {
        final SagaProcess sagaProcess = SagaProcessMock.create(TaskStatus.FAILED);

        when(sagaProcessRepository.findByOrderId(Mockito.any())).thenReturn(sagaProcess);

        final ResultDto resultDto = ResultDtoMock.create();
        resultDto.setStatus(TaskStatus.FAILED);
        sagaService.nextSagaStep(resultDto);

        verify(sagaProcessRepository).save(any());
    }

    @Test
    void cancelSaga() {
    }

    @Test
    void submitRevert() {
    }

    @Test
    void submitNextStep() {
    }
}