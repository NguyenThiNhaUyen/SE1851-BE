package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBloodRequestDTO {

    private Long requesterId;
    private String patientRecordCode;

    private Long doctorId;

    // ===== Thông tin bệnh nhân
    private String citizenId;
    private String patientName;
    private String patientPhone;
    private Integer patientAge;
    private String patientGender;
    private Double patientWeight;
    private String patientBloodGroup;

    // ===== Chi tiết truyền máu
    private Long bloodTypeId;
    private Long expectedBloodTypeId;
    private Long componentId;
    private Integer quantityBag;
    private Integer quantityMl;
    private String urgencyLevel;
    private String triageLevel;
    private String reason;
    private LocalDateTime neededAt;

    // ===== Lịch sử y khoa
    private Boolean crossmatchRequired;
    private Boolean hasTransfusionHistory;
    private Boolean hasReactionHistory;
    private Boolean isPregnant;
    private Boolean hasAntibodyIssue;

    // ===== Cảnh báo & ghi chú
    private String warningNote;
    private String specialNote;

    // ===== Metadata khác
    private Boolean isUnmatched;
    private Long codeRedId;
    private Boolean isAnonymousPatient;
    private Long suspectedPatientId;
    private String emergencyNote;

    // ===== Thông tin thanh toán
    private Boolean deferredPayment;
    private String deferredPaymentReason;

    // ===== Ưu tiên nội bộ (mới thêm)
    private String internalPriorityCode; // ✅ BỔ SUNG
}
