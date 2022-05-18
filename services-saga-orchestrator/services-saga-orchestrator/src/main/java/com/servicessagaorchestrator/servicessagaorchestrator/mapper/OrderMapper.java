package com.servicessagaorchestrator.servicessagaorchestrator.mapper;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderStatusDto;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author M.Bezmen
 */
@Mapper
public interface OrderMapper {
    @Mapping(target = "id", expression = "java(order.getId().toString())")
    OrderDto toDto(Order order);

    @Mapping(target = "id", expression = "java(order.getId().toString())")
    OrderStatusDto toStatusDto(Order order);
}
