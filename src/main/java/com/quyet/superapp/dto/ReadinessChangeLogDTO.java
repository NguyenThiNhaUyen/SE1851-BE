package com.quyet.superapp.dto;

import com.quyet.superapp.enums.DonorReadinessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReadinessChangeLogDTO {
    private DonorReadinessLevel fromLevel;
    private DonorReadinessLevel toLevel;
    private LocalDateTime changedAt;
}
