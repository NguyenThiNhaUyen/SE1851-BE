<<<<<<< HEAD
//package com.quyet.superapp.entity;
//
//import com.quyet.superapp.enums.RequestStatus;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "BloodRequests")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class BloodRequest {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "BloodRequestID")
//    private Long bloodRequestId;
//
//    @Column(name = "patient_record_code", unique = true, length = 20)
//    private String patientRecordCode;
//
//    // --- Liên kết với bác sĩ phụ trách ---
//    @ManyToOne
//    @JoinColumn(name = "DoctorId")
//    private User doctor;
//
//    // --- Liên kết với bệnh nhân ẩn danh (nếu có) ---
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "PatientUserId")
//    private User patient;
//
//    // --- Người gửi yêu cầu ---
//    @Column(name = "RequesterName", columnDefinition = "NVARCHAR(100)")
//    private String requesterName;
//
//    @Column(name = "RequesterPhone", columnDefinition = "NVARCHAR(20)")
//    private String requesterPhone;
//
//    // --- Thông tin bệnh nhân ---
//    @Column(name = "PatientName", columnDefinition = "NVARCHAR(100)")
//    private String patientName;
//
//    @Column(name = "PatientPhone", columnDefinition = "NVARCHAR(20)")
//    private String patientPhone;
//
//    @Column(name = "PatientAge")
//    private Integer patientAge;
//
//    @Column(name = "PatientGender", columnDefinition = "NVARCHAR(10)")
//    private String patientGender;
//
//    @Column(name = "PatientWeight")
//    private Double patientWeight;
//
//    @Column(name = "PatientBloodGroup", columnDefinition = "NVARCHAR(5)")
//    private String patientBloodGroup;
//
//    // --- Truyền máu ---
//    @ManyToOne
//    @JoinColumn(name = "BloodType")
//    private BloodType bloodType;
//
//    @ManyToOne
//    @JoinColumn(name = "ComponentID")
//    private BloodComponent component;
//
//    @Column(name = "QuantityBag")
//    private Integer quantityBag;
//
//    @Column(name = "QuantityML")
//    private Integer quantityMl;
//
//    // 🧑‍💼 Liên kết người gửi (staff)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "RequesterId")
//    private User requester;
//
//
//    @Column(name = "UrgencyLevel", columnDefinition = "NVARCHAR(20)")
//    private String urgencyLevel;
//
//    @Column(name = "TriageLevel", columnDefinition = "NVARCHAR(20)")
//    private String triageLevel;
//
//    @Column(name = "Reason", columnDefinition = "NVARCHAR(255)")
//    private String reason;
//
//    @Column(name = "NeededAt")
//    private LocalDateTime neededAt;
//
//    // --- Lịch sử y khoa ---
//    @Column(name = "CrossmatchRequired")
//    private Boolean crossmatchRequired;
//
//    @Column(name = "HasTransfusionHistory")
//    private Boolean hasTransfusionHistory;
//
//    @Column(name = "HasReactionHistory")
//    private Boolean hasReactionHistory;
//
//    @Column(name = "IsPregnant")
//    private Boolean isPregnant;
//
//    @Column(name = "HasAntibodyIssue")
//    private Boolean hasAntibodyIssue;
//
//    // --- Ghi chú & cảnh báo ---
//    @Column(name = "WarningNote", columnDefinition = "NVARCHAR(255)")
//    private String warningNote;
//
//    @Column(name = "SpecialNote", columnDefinition = "NVARCHAR(255)")
//    private String specialNote;
//
//    // --- Trạng thái & metadata ---
//    @Enumerated(EnumType.STRING)
//    @Column(name = "Status", columnDefinition = "NVARCHAR(20)")
//    private RequestStatus status;
//
//
//    @Column(name = "ConfirmedVolumeML")
//    private Integer confirmedVolumeMl;
//
//    @Column(name = "IsUnmatched")
//    private Boolean isUnmatched;
//
//    @Column(name = "CodeRedID")
//    private Long codeRedId;
//
//    @Column(name = "EmergencyNote", columnDefinition = "NVARCHAR(255)")
//    private String emergencyNote;
//
//    @Column(name = "ApprovedBy", columnDefinition = "NVARCHAR(50)")
//    private String approvedBy;
//
//    @Column(name = "ApprovedAt")
//    private LocalDateTime approvedAt;
//
//    @Column(name = "CreatedAt")
//    private LocalDateTime createdAt;
//
//    @PrePersist
//    public void onCreate() {
//        this.createdAt = LocalDateTime.now();
//    }
//
//    @Column(name = "UpdatedAt")
//    private LocalDateTime updatedAt;
//
//    @PreUpdate
//    public void onUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "suspected_patient_id")
//    private User suspectedPatient;
//
//
//    @Column(name = "payment_status", columnDefinition = "NVARCHAR(20)")
//    private String paymentStatus;
//
//    @Column(name = "total_amount")
//    private Integer totalAmount;
//
//    @Column(name = "deferred_payment")
//    private Boolean deferredPayment;
//
//    @Column(name = "deferred_payment_reason", columnDefinition = "NVARCHAR(255)")
//    private String deferredPaymentReason;
//
//    @Column(name = "cancel_reason", columnDefinition = "NVARCHAR(255)")
//    private String cancelReason;
//
//    @Column(name = "RequestCode", unique = true, length = 30)
//    private String requestCode;
//
//
//}


package com.quyet.superapp.entity;

import com.quyet.superapp.enums.BloodRequestStatus;
import com.quyet.superapp.enums.PaymentStatus;
import com.quyet.superapp.enums.UrgencyLevel;
import jakarta.persistence.*;
import lombok.*;
import com.quyet.superapp.enums.UrgencyLevel;
import com.quyet.superapp.entity.BloodType;

import java.time.LocalDateTime;

@Entity
@Table(name = "BloodRequests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodRequest {

    // 🔑 ID tự tăng duy nhất
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👤 Bệnh nhân liên kết
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // 🧑‍⚕️ Nhân viên gửi đơn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    // 👨‍⚕️ Bác sĩ phụ trách
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    // 🩸 Nhóm máu & thành phần máu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_type_id", nullable = false)
    private BloodType bloodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", nullable = false)
    private BloodComponent component;

    // 🩸 Nhóm máu mong muốn (nếu có)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expected_blood_type_id")
    private BloodType expectedBloodType;

    // 📝 Mã nội bộ, tự sinh bên Service
    private String patientRecordCode;  // Mã hồ sơ bệnh nhân
    private String requestCode;        // Mã đơn truyền máu

    private String reason;             // Lý do truyền máu
    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level") // để rõ ràng, mapping tên cột
    private UrgencyLevel urgencyLevel;

    private String triageLevel;        // Mức độ ưu tiên

    private Integer quantityBag;       // Số túi máu yêu cầu
    private Integer quantityMl;        // Số ml máu (nếu có)

    private Integer confirmedVolumeMl;   // ✅ sau khi truyền thực tế
    private String emergencyNote;        // ✅ lý do cấp cứu cụ thể
    private Integer totalAmount;         // ✅ chi phí dịch vụ

    private LocalDateTime neededAt;      // ✅ thời gian cần máu
    private LocalDateTime approvedAt;    // ✅ thời gian duyệt
    private LocalDateTime createdAt;     // ✅ thời gian tạo đơn

    // ⚙️ Thông tin y tế bổ sung
    private Boolean crossmatchRequired;
    private Boolean hasTransfusionHistory;
    private Boolean hasReactionHistory;
    private Boolean isPregnant;
    private Boolean hasAntibodyIssue;

    private String warningNote;
    private String specialNote;
    // entity BloodRequest
    private String internalPriorityCode;


    private Boolean deferredPayment;         // ✅ có cho nợ chi phí không
    private String deferredPaymentReason;    // ✅ lý do cho nợ

    private Boolean isUnmatched;             // ✅ truyền không tương thích
    private Long codeRedId;                  // ✅ mã định danh cấp cứu nếu có

    @Enumerated(EnumType.STRING)
    private BloodRequestStatus status;       // Trạng thái đơn

    private String requesterName;            // ✅ tên người gửi đơn (hiển thị)
    private String requesterPhone;           // ✅ số điện thoại gửi đơn

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;     // ✅ trạng thái thanh toán

    private LocalDateTime updatedAt;
    private String cancelReason;             // ✅ lý do từ chối/hủy
    private LocalDateTime cancelledAt;       // ✅ thời gian hủy


}

=======
    package com.quyet.superapp.entity;

    import jakarta.persistence.*;
    import lombok.*;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "BloodRequests")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class BloodRequest {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "BloodRequestID")
        private Long bloodRequestId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "RequesterID")
        private User requester;

        @ManyToOne
        @JoinColumn(name = "BloodType", referencedColumnName = "BloodTypeID")
        private BloodType bloodType;

        @ManyToOne
        @JoinColumn(name = "ComponentID")
        private BloodComponent component;

        @Column(name = "QuantityML")
        private Integer quantityMl;

        @Column(name = "UrgencyLevel", columnDefinition = "NVARCHAR(20)")
        private String urgencyLevel;

        @Column(name = "Status", columnDefinition = "NVARCHAR(20)")
        private String status;

        @Column(name = "CreatedAt", columnDefinition = "DATETIME")
        private LocalDateTime createdAt;

        @Column(name = "ConfirmedVolumeML")
        private Integer confirmedVolumeMl;

        @Column(name = "Reason", columnDefinition = "NVARCHAR(255)")
        private String reason;

        @Column(name = "IsUnmatched")
        private Boolean isUnmatched;

        @Column(name = "TriageLevel", columnDefinition = "NVARCHAR(20)")
        private String triageLevel;

        @Column(name = "CodeRedID")
        private Long codeRedId;

        @Column(name = "EmergencyNote", columnDefinition = "NVARCHAR(255)")
        private String emergencyNote;

        @Column(name = "ApprovedBy", columnDefinition = "NVARCHAR(50)")
        private String approvedBy;

        @Column(name = "ApprovedAt")
        private LocalDateTime approvedAt;


    }
>>>>>>> origin/main
