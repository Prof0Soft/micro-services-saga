package com.serviceb.storeroom.mapper;


import com.serviceb.storeroom.dto.ItemReservationDto;
import com.serviceb.storeroom.entity.ItemReservation;
import org.mapstruct.Mapper;

@Mapper
public interface ItemReservationMapper {

    ItemReservation toEntity(ItemReservationDto order);

    ItemReservationDto toDto(ItemReservation order);
}
