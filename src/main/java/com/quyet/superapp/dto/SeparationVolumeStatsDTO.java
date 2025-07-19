package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BloodComponentType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeparationVolumeStatsDTO {
    private String date; // "2025-06-29" hoặc "2025-06"
    private BloodComponentType componentType; // Kiểu thành phần máu
    private Long totalVolume; // Tổng thể tích máu tách ra


    // Constructor phải nhận đủ 3 tham số
    public SeparationVolumeStatsDTO(String date, BloodComponentType componentType, Long totalVolume) {
        this.date = date;
        this.componentType = componentType;
        this.totalVolume = totalVolume;
    }
}



