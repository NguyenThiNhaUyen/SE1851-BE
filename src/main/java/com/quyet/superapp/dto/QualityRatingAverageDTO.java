package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BloodComponentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityRatingAverageDTO {
    private BloodComponentType componentType;
    private Double averageRating; // nếu chuẩn hóa A=4, B=3...
}
