package com.servicea.order.mapper;

import com.servicea.order.dto.OrderDto;
import com.servicea.order.entity.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

    Order toEntity(OrderDto order);

    OrderDto toDto(Order order);
}
