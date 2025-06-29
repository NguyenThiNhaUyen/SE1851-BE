package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BloodComponentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodSeparationDetailFullDTO {
    private Long bloodSeparationDetailId;
    private BloodComponentType componentType;
    private Integer volumeMl;
    private String qualityRating;

    // Thông tin mở rộng từ SeparationResult
    private Long separationResultId;
    private String bloodBagCode;
    private String processedBy;

}
