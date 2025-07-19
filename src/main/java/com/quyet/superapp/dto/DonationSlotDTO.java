package com.quyet.superapp.dto;

import com.quyet.superapp.enums.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationSlotDTO {
    private Long slotId;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxCapacity;
    private String location;
    private String notes;
    private Integer registeredCount;
    private SlotStatus status;
}
