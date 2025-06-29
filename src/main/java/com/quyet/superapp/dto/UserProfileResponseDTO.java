package com.quyet.superapp.dto;

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
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String bloodType;
    private String phone;
    private String landline;
    private String email;
    private String occupation;
    private LocalDate lastDonationDate;
    private Integer recoveryTime;
    private String location;
    private String citizenId;
    private Double weight;
    private Double height;

    private String addressFull;
    private Long addressId;
    private Double latitude;
    private Double longitude;
}
