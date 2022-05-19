package com.serviceb.storeroom.service;


import com.serviceb.storeroom.dto.ItemReservationDto;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
public interface ItemReservationService {

    ItemReservationDto create(ItemReservationDto order);

    void removeOrderByTaskId(UUID taskId);
}
