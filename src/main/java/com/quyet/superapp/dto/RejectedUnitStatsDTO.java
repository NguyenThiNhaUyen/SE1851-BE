package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BloodComponentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RejectedUnitStatsDTO {
    private BloodComponentType componentType;
    private Long totalCount;
    private Long rejectedCount;
    private Double rejectionRate; // %
}
