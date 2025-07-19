package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.DonationSlotDTO;
import com.quyet.superapp.entity.DonationSlot;
import org.springframework.stereotype.Component;

@Component
public class DonationSlotMapper {

    public static DonationSlotDTO toDTO(DonationSlot entity) {
        if (entity == null) return null;
        return DonationSlotDTO.builder()
                .slotId(entity.getSlotId())
                .slotDate(entity.getSlotDate())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .maxCapacity(entity.getMaxCapacity())
                .location(entity.getLocation())
                .notes(entity.getNotes())
                .registeredCount(entity.getRegisteredCount())
                .status(entity.getStatus())
                .build();
    }

    public static DonationSlot toEntity(DonationSlotDTO dto) {
        if (dto == null) return null;
        return DonationSlot.builder()
                .slotId(dto.getSlotId())
                .slotDate(dto.getSlotDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .maxCapacity(dto.getMaxCapacity())
                .location(dto.getLocation())
                .notes(dto.getNotes())
                .registeredCount(dto.getRegisteredCount())
                .status(dto.getStatus())
                .build();
    }
}

