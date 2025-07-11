package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnverifiedDonorDTO {
    private Long donorRegistryId;
    private Long userId;
    private String fullName;
    private String phone;
    private String bloodType;
    private String gender;
    private LocalDate dob;
    private String location;
    private String addressFull;
    private LocalDateTime registeredAt;
    private String status;
}

