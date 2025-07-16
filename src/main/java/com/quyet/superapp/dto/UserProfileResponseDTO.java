package com.quyet.superapp.dto;

import com.quyet.superapp.entity.BloodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private String role;
    private boolean enable;

    private String fullName;
    private LocalDate dob;
    private String gender;
    private BloodType bloodType;
    private String phone;
    private String landline;
    private String occupation;
    private String citizenId;
    private Double weight;
    private Double height;
    private String emergencyContact;
    private String altPhone;
    private String location;
    private LocalDate lastDonationDate;
    private Integer recoveryTime;

    private String addressFull;
    private Long addressId;
    private Double latitude;
    private Double longitude;

    private boolean hasInsurance;
    private String insuranceCardNumber;
    private LocalDate insuranceValidTo;
}
