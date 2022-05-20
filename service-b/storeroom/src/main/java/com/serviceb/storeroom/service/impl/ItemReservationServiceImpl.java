package com.serviceb.storeroom.service.impl;


import com.serviceb.storeroom.dto.ItemReservationDto;
import com.serviceb.storeroom.entity.ItemReservation;
import com.serviceb.storeroom.entity.Task;
import com.serviceb.storeroom.mapper.ItemReservationMapper;
import com.serviceb.storeroom.repository.ItemReservationRepository;
import com.serviceb.storeroom.service.ItemReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Slf4j
@Component
public class ItemReservationServiceImpl implements ItemReservationService {
    private final ItemReservationRepository itemReservationRepository;
    private final ItemReservationMapper itemReservationMapper;

    public ItemReservationServiceImpl(final ItemReservationRepository itemReservationRepository,
                                      final ItemReservationMapper itemReservationMapper) {
        this.itemReservationRepository = itemReservationRepository;
        this.itemReservationMapper = itemReservationMapper;
    }

    @Transactional
    @Override
    public ItemReservationDto create(ItemReservationDto itemReservationDto) {

        Task task = new Task();
        task.setId(itemReservationDto.getTaskId());

        ItemReservation order = new ItemReservation();
        order.setTask(task);
        order = itemReservationRepository.save(order);
        return itemReservationMapper.toDto(order);
    }

    @Transactional
    @Override
    public void removeByTaskId(final UUID taskId) {
        itemReservationRepository.removeByTaskId(taskId);
    }
}
