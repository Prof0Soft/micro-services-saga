package com.serviceb.storeroom.repository;

import com.serviceb.storeroom.entity.ItemReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Repository
public interface ItemReservationRepository extends JpaRepository<ItemReservation, UUID> {
    @Modifying
    void removeByTaskId(UUID taskId);
}
