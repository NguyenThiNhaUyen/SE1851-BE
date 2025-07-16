package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "HealthCheckForms")
public class HealthCheckForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_check_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false)
    private DonationRegistration registration;

    // 🔹 Thông số sinh tồn
    @Column(name = "body_temperature")
    private Double bodyTemperature;

    @Column(name = "heart_rate")
    private Integer heartRate;

    @Column(name = "blood_pressure_sys")
    private Integer bloodPressureSys;

    @Column(name = "blood_pressure_dia")
    private Integer bloodPressureDia;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "height_cm")
    private Double heightCm;

    // 🔹 Câu hỏi loại trừ
    private Boolean hasFever;
    private Boolean tookAntibioticsRecently;
    private Boolean hasChronicIllness;
    private Boolean isPregnantOrBreastfeeding;
    private Boolean hadRecentTattooOrSurgery;
    private Boolean hasRiskySexualBehavior;

    // 🔹 Kết quả tự động đánh giá
    @Column(name = "is_eligible")
    private Boolean isEligible;

    @Column(name = "notes_by_staff", columnDefinition = "NVARCHAR(500)")
    private String notesByStaff;

    // 🔹 Kết nối với bản ghi hiến máu nếu đã đủ điều kiện
    @ManyToOne
    @JoinColumn(name = "donation_id")
    private Donation donation;

    // 🔬 Xét nghiệm máu gộp vào đây
    @Column(name = "hemoglobin")
    private Double hemoglobin;

    @Column(name = "hbs_ag_positive")
    private Boolean hbsAgPositive;

    @Column(name = "hcv_positive")
    private Boolean hcvPositive;

    @Column(name = "hiv_positive")
    private Boolean hivPositive;

    @Column(name = "syphilis_positive")
    private Boolean syphilisPositive;
}
