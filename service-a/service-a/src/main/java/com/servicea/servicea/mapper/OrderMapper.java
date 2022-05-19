package com.servicea.servicea.mapper;

import com.servicea.servicea.dto.OrderDto;
import com.servicea.servicea.entity.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

    Order toEntity(OrderDto order);

    OrderDto toDto(Order order);
}
