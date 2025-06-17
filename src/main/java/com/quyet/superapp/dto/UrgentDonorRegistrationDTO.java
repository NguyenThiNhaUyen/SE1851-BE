package com.quyet.superapp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UrgentDonorRegistrationDTO {
    private Long userId;
    private Long bloodTypeId;
    private AddressRequestDTO addressRequest; // Địa chỉ (City, District, Ward, etc.)
    private String location;
    private Float latitude;
    private Float longitude;

    // BỔ SUNG để service hoạt động đúng:
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String phone;
}
