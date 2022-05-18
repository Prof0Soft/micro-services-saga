package com.servicessagaorchestrator.servicessagaorchestrator.service.impl;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderStatusDto;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Order;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Step;
import com.servicessagaorchestrator.servicessagaorchestrator.mapper.OrderMapper;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.SagaProcessRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.service.OrderService;
import com.servicessagaorchestrator.servicessagaorchestrator.service.SagaService;
import com.servicessagaorchestrator.servicessagaorchestrator.type.ServiceName;
import com.servicessagaorchestrator.servicessagaorchestrator.type.TaskStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Sergey B.
 * 18.05.2022
 */
@Service
public class OrderServiceImpl implements OrderService {
    private final SagaService sagaService;
    private final SagaProcessRepository sagaProcessRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(final SagaProcessRepository sagaProcessRepository,
                            final OrderMapper orderMapper,
                            final SagaService sagaService) {
        this.sagaProcessRepository = sagaProcessRepository;
        this.orderMapper = orderMapper;
        this.sagaService = sagaService;
    }

    @Transactional
    @Override
    public OrderDto createOrder() {
        final Order orderEntity = new Order();
        final SagaProcess sagaProcess = new SagaProcess();
        sagaProcess.setStatus(TaskStatus.NEW);
        sagaProcess.setOrder(orderEntity);
        sagaProcess.setSteps(buildFlow());

        final SagaProcess saved = sagaProcessRepository.save(sagaProcess);

        sagaService.startSaga(saved);

        return orderMapper.toDto(saved.getOrder());
    }

    @Override
    public OrderStatusDto getOrderStatus(final String id) {
        // todo: взять статус из базы и отдать
        return null;
    }

    @Override
    public OrderStatusDto cancelOrder(final String id) {
        return null;
    }

    private List<Step> buildFlow() {
        final Step one = new Step();
        one.setFlowOrder(1);
        one.setStatus(TaskStatus.NEW);
        one.setServiceName(ServiceName.SERVICE_A);

        final Step two = new Step();
        two.setFlowOrder(2);
        two.setStatus(TaskStatus.NEW);
        two.setServiceName(ServiceName.SERVICE_B);

        final Step three = new Step();
        three.setFlowOrder(3);
        three.setStatus(TaskStatus.NEW);
        three.setServiceName(ServiceName.SERVICE_C);

        return List.of(one, two, three);
    }
}
