package com.quyet.superapp.dto;

import com.quyet.superapp.enums.DonorReadinessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class CurrentUrgentDonorStatusDTO {
    private DonorReadinessLevel mode;
    private LocalDateTime registeredAt;
    private Boolean isVerified;
    private LocalDateTime leftGroupAt;
}
