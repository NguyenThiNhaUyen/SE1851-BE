package com.quyet.superapp.repository;

import com.quyet.superapp.entity.DonationSlot;
import com.quyet.superapp.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DonationSlotRepository extends JpaRepository<DonationSlot, Long> {
    List<DonationSlot> findBySlotDate(LocalDate date);
    List<DonationSlot> findByStatus(SlotStatus status);
    List<DonationSlot> findBySlotDateAndStatusAndRegisteredCountLessThan(
            LocalDate slotDate,
            SlotStatus status,
            int maxCapacity
    );
    List<DonationSlot> findByStatusAndRegisteredCountLessThan(SlotStatus status, int registeredCount);

    List<DonationSlot> findBySlotDateAndLocationAndStatus(LocalDate slotDate, String location, SlotStatus status);

}
