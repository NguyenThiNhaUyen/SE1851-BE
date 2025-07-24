package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BloodComponentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodSeparationDetailDTO {
    private Long bloodSeparationDetailId;

    @NotNull(message = "Loại thành phần không được để trống")
    private BloodComponentType componentType;

    @NotNull(message = "Thể tích không được để trống")
    @Min(value = 10, message = "Thể tích phải >= 10ml")
    private Integer volumeMl;

    private String qualityRating;

    private String note;                  // ✅ Ghi chú thêm nếu có

    private LocalDateTime createdAt;      // ✅ Thời gian tạo
}
