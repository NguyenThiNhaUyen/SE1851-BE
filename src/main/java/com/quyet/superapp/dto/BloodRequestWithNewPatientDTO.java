package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

//@Data
//public class BloodRequestWithNewPatientDTO {
//
//    // ✅ Thêm CCCD để tra cứu
//    private String citizenId;
//
//    // ✅ Thêm suspectedPatientId nếu đã tìm được
//    private Long suspectedPatientId;
//
//    @NotBlank
//    private String patientName;
//    @NotBlank
//    private String patientPhone;
//    @NotNull
//    private Integer patientAge;
//    @NotBlank
//    private String patientGender;
//    @NotNull
//    private Double patientWeight;
//    @NotBlank
//    private String patientBloodGroup;
//
//    @NotBlank
//    private String requesterName;
//    @NotBlank
//    private String reason;
//    @NotBlank
//    private String urgencyLevel;
//    @NotBlank
//    private String triageLevel;
//    @NotNull
//    private Integer quantityBag;
//    @NotNull
//    private Integer quantityMl;
//    @NotBlank
//    private String componentName;
//    @NotNull
//    private LocalDateTime neededAt;
//
//    private Boolean crossmatchRequired;
//    private Boolean hasTransfusionHistory;
//    private Boolean hasReactionHistory;
//    private Boolean isPregnant;
//    private Boolean hasAntibodyIssue;
//
//    private String warningNote;
//    private String specialNote;
//
//    private String patientRecordCode;
//
//    private Boolean deferredPayment;
//    private String deferredPaymentReason;
//
//}


@Data
public class BloodRequestWithNewPatientDTO {

    // 🔎 Tra cứu bệnh nhân
    private String citizenId;
    private Long suspectedPatientId;

    // 👤 Thông tin bệnh nhân
    @NotBlank
    private String patientName;
    @NotBlank
    private String patientPhone;
    @NotNull
    private Integer patientAge;
    @NotBlank
    private String patientGender;
    @NotNull
    private Double patientWeight;
    @NotBlank
    private String patientBloodGroup;

    // 🧑‍⚕️ Người gửi & bác sĩ (dùng ID thay vì name)
    @NotNull
    private Long requesterId;
    @NotNull
    private Long doctorId;

    // 🩸 Yêu cầu máu
    @NotBlank
    private String reason;
    @NotBlank
    private String urgencyLevel;
    @NotBlank
    private String triageLevel;
    @NotNull
    private Integer quantityBag;
    @NotNull
    private Integer quantityMl;

    @NotNull
    private Long bloodTypeId;
    @NotNull
    private Long componentId;

    private Long expectedBloodTypeId;        // 🆕 nếu muốn truyền nhóm thay thế
    private String priorityCode;             // 🆕 mã RED/VIP...

    @NotNull
    private LocalDateTime neededAt;

    // 🧪 Y tế nâng cao
    private Boolean crossmatchRequired;
    private Boolean hasTransfusionHistory;
    private Boolean hasReactionHistory;
    private Boolean isPregnant;
    private Boolean hasAntibodyIssue;

    private String warningNote;
    private String specialNote;

    private String patientRecordCode;        // nếu có thì dùng, không thì hệ thống sinh

    // 💰 Thanh toán
    private Boolean deferredPayment;
    private String deferredPaymentReason;
}
