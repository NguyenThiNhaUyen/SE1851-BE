package com.quyet.superapp.dto;

import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.enums.BloodBagStatus;
import com.quyet.superapp.enums.RhType;
import com.quyet.superapp.enums.TestStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodBagDTO {
    @NotNull
    private Long bloodBagId;
    @NotBlank
    private String bagCode;         // Mã túi máu (duy nhất)
    @NotBlank
    private String bloodType;  // ← "A+", "O−", v.v.      // Nhóm máu (A, B, AB, O)
    @NotNull
    private RhType rh;              // Rh (+ hoặc -)
    @Min(0)
    private Integer volume;         // Tổng thể tích máu
    private Double hematocrit;      // Hct (nếu có)
    private LocalDateTime collectedAt;
    private TestStatus testStatus;  // Chưa xét nghiệm, âm tính, dương tính
    private BloodBagStatus status;  // AVAILABLE, USED, EXPIRED...
    @NotBlank
    private String donorId;         // Mã người hiến máu

    private String note;            // Ghi chú tùy ý
}
