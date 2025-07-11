package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBloodRequestDTO {

    private Long requesterId; // ID của staff, sẽ dùng để truy User từ DB
    private String patientRecordCode;

    private Long doctorId; // bác sĩ phụ trách bệnh nhân


    // ===== Thông tin bệnh nhân
    private String citizenId; // 🆔 CCCD để kiểm tra trùng bệnh nhân
    private String patientName;
    private String patientPhone;
    private Integer patientAge;
    private String patientGender;
    private Double patientWeight;
    private String patientBloodGroup;

    // ===== Chi tiết truyền máu
    private Long bloodTypeId;
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

    private Boolean isAnonymousPatient; // true nếu không rõ danh tính
    private Long suspectedPatientId;    // ID bệnh nhân nghi ngờ trùng
    private String emergencyNote;       // Mô tả như: "Mất ý thức, nghi là BN Nguyễn Văn A"

    // ===== Thông tin thanh toán
    private Boolean deferredPayment;          // Có thanh toán sau không?
    private String deferredPaymentReason;     // Lý do thanh toán sau

}

