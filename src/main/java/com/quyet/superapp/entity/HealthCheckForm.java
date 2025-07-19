package com.quyet.superapp.entity;

import jakarta.persistence.*;
<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import lombok.*;
>>>>>>> origin/main

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
<<<<<<< HEAD
@Table(name = "HealthCheckForms")
@Builder
=======
@Builder
@Table(name = "HealthCheckForms")
>>>>>>> origin/main
public class HealthCheckForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_check_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false)
    private DonationRegistration registration;

<<<<<<< HEAD
    //  Thông số sinh tồn
    @Column(name = "body_temperature")
    private Double bodyTemperature;  // Nhiệt độ cơ thể (°C)

    @Column(name = "heart_rate")
    private Integer heartRate;  // Mạch (lần/phút)

    @Column(name = "blood_pressure_sys")
    private Integer bloodPressureSys;  // Huyết áp tâm thu

    @Column(name = "blood_pressure_dia")
    private Integer bloodPressureDia;   // Huyết áp tâm trương

    @Column(name = "weight_kg")
    private Double weightKg;     // Cân nặng (kg)

    // 🚫 Câu hỏi loại trừ
    @Column(name = "has_fever")
    private Boolean hasFever;

    @Column(name = "took_antibiotics_recently")
    private Boolean tookAntibioticsRecently;

    @Column(name = "has_chronic_illness")
    private Boolean hasChronicIllness;

    @Column(name = "is_pregnant_or_breastfeeding")
    private Boolean isPregnantOrBreastfeeding;

    @Column(name = "had_recent_tattoo_or_surgery")
    private Boolean hadRecentTattooOrSurgery;

    @Column(name = "has_risky_sexual_behavior")
    private Boolean hasRiskySexualBehavior;

    // ✅ Kết quả cuối cùng
=======
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
>>>>>>> origin/main
    @Column(name = "is_eligible")
    private Boolean isEligible;

    @Column(name = "notes_by_staff", columnDefinition = "NVARCHAR(500)")
    private String notesByStaff;

<<<<<<< HEAD
    @ManyToOne
    @JoinColumn(name = "donation_id")
    private Donation donation;
=======
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
>>>>>>> origin/main
}
