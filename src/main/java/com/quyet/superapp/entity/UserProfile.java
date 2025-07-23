package com.quyet.superapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quyet.superapp.entity.address.Address;
import jakarta.persistence.*;
import lombok.*;
<<<<<<< HEAD
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
=======
>>>>>>> origin/main

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
<<<<<<< HEAD
@Table(name = "UserProfile", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"citizen_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
=======
@Table(name = "UserProfile")
@Data
@NoArgsConstructor
@AllArgsConstructor
>>>>>>> origin/main
public class UserProfile {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "User_Id")
    @JsonIgnore
    private User user;

<<<<<<< HEAD
    @Column(name = "full_name", columnDefinition = "NVARCHAR(50)")
    private String fullName;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "gender", columnDefinition = "NVARCHAR(10)")
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "phone", columnDefinition = "VARCHAR(20)")
    private String phone;

    @Column(name = "email", columnDefinition = "VARCHAR(100)")
    private String email;

    @Column(name = "citizen_id", columnDefinition = "VARCHAR(12)", nullable = false)
    private String citizenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_type_id")
    private BloodType bloodType;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "height")
    private Double height;

    @Column(name = "donation_date")
    private LocalDateTime lastDonationDate;

    @Column(name = "recovery_time")
    private Integer recoveryTime;

    @Column(name = "location", columnDefinition = "NVARCHAR(100)")
    private String location;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "staff_position", columnDefinition = "NVARCHAR(30)")
    private String staffPosition;

    @Column(name = "occupation", columnDefinition = "NVARCHAR(50)")
    private String occupation;

    @Column(name = "note", columnDefinition = "NVARCHAR(255)")
    private String note;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    // === Bảo hiểm y tế ===
    @Column(name = "has_insurance")
    private Boolean hasInsurance;  // TRUE / FALSE / NULL
=======
    @Column(name = "full_name", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String fullName;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "gender", nullable = false, columnDefinition = "NVARCHAR(10)")
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "phone", nullable = false, columnDefinition = "VARCHAR(20)")
    private String phone;

    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(100)")
    private String email;

    @Column(name = "occupation", columnDefinition = "NVARCHAR(50)")
    private String occupation;

    @Column(name = "citizen_id", nullable = false, columnDefinition = "VARCHAR(12)", unique = true)
    private String citizenId;

    // ✅ Các trường y tế – tùy chọn
    @ManyToOne
    @JoinColumn(name = "blood_type")
    private BloodType bloodType;

    private Double WeightKg;
    private Double HeightCm;
    private LocalDate lastDonationDate;
    @Column(name = "recovery_time")
    private LocalDate recoveryTime;
    private String location;
    private Double latitude;
    private Double longitude;
    private String landline;
    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "alt_phone")
    private String altPhone;

    // ✅ Thông tin BHYT
    @Column(name = "has_insurance")
    private boolean hasInsurance;
>>>>>>> origin/main

    @Column(name = "insurance_card_number", columnDefinition = "VARCHAR(20)")
    private String insuranceCardNumber;

    @Column(name = "insurance_valid_to")
    private LocalDate insuranceValidTo;
<<<<<<< HEAD

    // === Helpers ===
    public String getFullAddressString() {
        if (address == null) return "Chưa cập nhật";
        return address.toString();
    }

    public boolean isInsuranceStillValid() {
        return Boolean.TRUE.equals(hasInsurance)
                && insuranceValidTo != null
                && !insuranceValidTo.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return fullName + " - " + (phone != null ? phone : "No phone");
    }

}
=======
}




>>>>>>> origin/main
