package com.quyet.superapp.dto;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonFormat;
=======
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
>>>>>>> origin/main
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
<<<<<<< HEAD
@Builder // ✅ Thêm dòng này
public class BloodRequestDTO {

    private Long bloodRequestId;

    private String patientRecordCode;

    // === Người yêu cầu
    private String requesterName;
    private String requesterPhone;


    // Bác sĩ phụ trách
    private String doctorName;
    private String doctorPhone;

    // === Bệnh nhân
    private String patientName;
    private String patientPhone;
    private Integer patientAge;
    private String patientGender;
    private Double patientWeight;
    private String patientBloodGroup;

    private Long suspectedPatientId;

    // === Truyền máu
    private String bloodTypeName;       // ✅ MỚI: Hiển thị tên loại máu

    private String componentName;       // ✅ MỚI: Hiển thị tên thành phần máu

    private Integer quantityBag;
    private Integer quantityMl;
    private String urgencyLevel;
    private String triageLevel;
    private String reason;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime neededAt;

    // === Lịch sử y khoa
    private Boolean crossmatchRequired;
    private Boolean hasTransfusionHistory;
    private Boolean hasReactionHistory;
    private Boolean isPregnant;
    private Boolean hasAntibodyIssue;

    // === Ghi chú & cảnh báo
    private String warningNote;
    private String specialNote;

    // === Metadata & xác nhận
    private String status;
    private Integer confirmedVolumeMl;
    private Boolean isUnmatched;
    private Long codeRedId;
    private String emergencyNote;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime approvedAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    private Integer totalAmount;
    private String paymentStatus;
    private Boolean deferredPayment;
    private String deferredPaymentReason;
    private String cancelReason;

    private String requestCode;


}
=======
public class BloodRequestDTO {
    private Long bloodRequestId;
    @NotNull(message = "Người yêu cầu không được để trống")
    private Long requesterId;

    @NotNull(message = "Nhóm máu không được để trống")
    private Long bloodTypeId;

    @NotNull(message = "Thành phần máu không được để trống")
    private Long componentId;

    @NotNull(message = "Số lượng máu yêu cầu không được để trống")
    @Min(value = 50, message = "Số lượng yêu cầu phải >= 50ml")
    private Integer quantityMl;

    @NotBlank(message = "Mức độ khẩn cấp không được để trống")
    private String urgencyLevel;

    private String status;

    private LocalDateTime createdAt;

    @Min(value = 0, message = "Lượng xác nhận phải >= 0ml")
    private Integer confirmedVolumeMl;

    private String reason;                 // Lý do truyền máu
    private Boolean isUnmatched;          // true nếu dùng máu O- chưa định nhóm
    private String triageLevel;           // RED, YELLOW, BLACK (cho Code Red)
    private Long codeRedId;               // Mã sự kiện Code Red nếu có
    private String emergencyNote;         // Ghi chú nếu là cấp cứu
    private String approvedBy;            // Tên admin duyệt
    private LocalDateTime approvedAt;     // Thời gian duyệt


}

>>>>>>> origin/main
