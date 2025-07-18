package com.quyet.superapp.dto;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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

    // ===== Thông tin BHYT
    private Boolean hasInsurance;
    private String insuranceCardNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate insuranceValidTo;

}
=======
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBloodRequestDTO {
    private Long requesterId;
    private Long bloodTypeId;
    private Long componentId;
    private Integer quantityMl;
    private String urgencyLevel;
    private String reason;

    // Optional for cấp cứu / code red
    private Boolean isUnmatched;
    private String triageLevel;
    private Long codeRedId;
}
>>>>>>> origin/main
