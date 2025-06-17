package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDTO {
    private Long userId;

    @NotBlank
    private String fullName;

    private LocalDate dob;
    private String gender;
    private String bloodType;
    private String address;
    private String phone;
    private String landline;
    private String email;
    private String occupation;
    private LocalDate lastDonationDate;
    private Integer recoveryTime;
    private String location;

    @NotBlank(message = "CCCD không được để trống")
    @Pattern(regexp = "\\d{12}", message = "CCCD phải gồm đúng 12 chữ số")
    private String citizenId;

}

