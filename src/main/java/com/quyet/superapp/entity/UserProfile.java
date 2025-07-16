package com.quyet.superapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quyet.superapp.entity.address.Address;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserProfile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "User_Id")
    @JsonIgnore
    private User user;

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
    private Integer recoveryTime;
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

    @Column(name = "insurance_card_number", columnDefinition = "VARCHAR(20)")
    private String insuranceCardNumber;

    @Column(name = "insurance_valid_to")
    private LocalDate insuranceValidTo;
}




