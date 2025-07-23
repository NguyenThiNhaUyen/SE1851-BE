package com.quyet.superapp.dto;

<<<<<<< HEAD
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.enums.BloodBagStatus;
import com.quyet.superapp.enums.TestStatus;
import lombok.AllArgsConstructor;
=======

import com.quyet.superapp.enums.BloodBagStatus;
import com.quyet.superapp.enums.RhType;
import com.quyet.superapp.enums.TestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
>>>>>>> origin/main
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
<<<<<<< HEAD
public class BloodBagDTO {
    private Long bloodBagId;
    private String bagCode;         // Mã túi máu (duy nhất)
    private String bloodType;  // ← "A+", "O−", v.v.      // Nhóm máu (A, B, AB, O)
    private String rh;              // Rh (+ hoặc -)
    private Integer volume;         // Tổng thể tích máu
    private Double hematocrit;      // Hct (nếu có)
    private LocalDateTime collectedAt;
    private TestStatus testStatus;  // Chưa xét nghiệm, âm tính, dương tính
    private BloodBagStatus status;  // AVAILABLE, USED, EXPIRED...
    private String donorId;         // Mã người hiến máu
    private String note;            // Ghi chú tùy ý
}
=======
@Builder
public class BloodBagDTO {
    private Long bloodBagId;
    private String bagCode;

    private Long bloodTypeId;
    private String bloodTypeName;

    private RhType rh;
    private Integer volume;
    private Double hematocrit;
    private LocalDateTime collectedAt;

    private TestStatus testStatus;
    private BloodBagStatus status;

    private Long donorId;
    private String donorName;
    private String donorPhone;

    private String note;
}

>>>>>>> origin/main
