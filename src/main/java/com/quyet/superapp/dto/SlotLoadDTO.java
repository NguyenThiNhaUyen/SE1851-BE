package com.quyet.superapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class SlotLoadDTO {
    private Long slotId;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int maxCapacity;
    private int registeredCount;
    private double loadPercent;
}
