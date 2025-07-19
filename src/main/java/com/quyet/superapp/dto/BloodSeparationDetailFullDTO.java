package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BloodComponentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodSeparationDetailFullDTO {
    private Long bloodSeparationDetailId;
    private BloodComponentType componentType;
    private Integer volumeMl;
    private String qualityRating;
    private String note;                  // ✅ Ghi chú thêm nếu có
    private LocalDateTime createdAt;

    // Thông tin mở rộng từ SeparationResult
    private Long separationResultId;
    private String bloodBagCode;
    private String processedBy;

}
