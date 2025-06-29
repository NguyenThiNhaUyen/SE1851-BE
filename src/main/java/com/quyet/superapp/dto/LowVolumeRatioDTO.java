package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BloodComponentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LowVolumeRatioDTO {
    private BloodComponentType componentType;
    private Long totalCount;
    private Long lowVolumeCount;
    private Double percentage; // tính %
}

