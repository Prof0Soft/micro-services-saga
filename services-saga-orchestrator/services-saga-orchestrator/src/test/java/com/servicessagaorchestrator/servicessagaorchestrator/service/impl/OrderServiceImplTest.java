package com.servicessagaorchestrator.servicessagaorchestrator.service.impl;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderStatusDto;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Order;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.BookingFlow;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
import com.servicessagaorchestrator.servicessagaorchestrator.exception.NotFoundException;
import com.servicessagaorchestrator.servicessagaorchestrator.mapper.OrderMapper;
import com.servicessagaorchestrator.servicessagaorchestrator.mockdata.SagaProcessMock;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.OrderRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.SagaProcessRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.service.OrderService;
import com.servicessagaorchestrator.servicessagaorchestrator.service.SagaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Sergey B.
 * 20.05.2022
 */
@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
class OrderServiceImplTest {
    @MockBean
    private SagaService sagaService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;
    @MockBean
    private SagaProcessRepository sagaProcessRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private AuditingHandler auditingHandler;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    void createOrder_success() {
        //GIVEN
        final SagaProcess sagaProcess = SagaProcessMock.create();

        when(sagaProcessRepository.save(Mockito.any())).thenReturn(sagaProcess);

        //WHEN
        final OrderDto resultOrder = orderService.createOrder();

        //THEN
        verify(sagaProcessRepository).save(any());
        verify(sagaService).initSaga(Mockito.any());
        assertNotNull(resultOrder);
        assertNotNull(resultOrder.getId());
    }

    @Test
    void getOrderStatus_Success() {
        //GIVEN
        final UUID orderId = UUID.fromString("23e4567-e89b-12d3-a456-426614174000");

        final OrderStatusDto expected = new OrderStatusDto();
        expected.setStatus(TaskStatus.RUNNING);
        expected.setId(orderId.toString());

        final Order order = new Order();
        order.setId(orderId);
        order.setStatus(TaskStatus.RUNNING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        //WHEN
        final OrderStatusDto actual = orderService.getOrderStatus(orderId);

        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void getOrderStatus_NotFound() {
        //GIVEN
        final String taskId = "23e4567-e89b-12d3-a456-426614174000";
        final UUID orderId = UUID.fromString(taskId);

        //WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        //THEN
        assertThrows(NotFoundException.class, () -> orderService.getOrderStatus(orderId));
    }
    @Test
    void cancelOrder() {
        //GIVEN
        final UUID orderId = UUID.fromString("23e4567-e89b-12d3-a456-426614174000");

        final OrderStatusDto expected = new OrderStatusDto();
        expected.setStatus(TaskStatus.RUNNING);
        expected.setId(orderId.toString());

        final Order order = new Order();
        order.setId(orderId);
        order.setStatus(TaskStatus.RUNNING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        //WHEN
        final OrderStatusDto actual = orderService.cancelOrder(orderId);

        //THEN
        verify(sagaService).cancelSaga(orderId);
        assertEquals(expected, actual);
    }
}