package com.quyet.superapp.dto;

<<<<<<< HEAD
import com.quyet.superapp.enums.DonorReadinessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import lombok.Data;
>>>>>>> origin/main

import java.time.LocalDate;

@Data
<<<<<<< HEAD
@AllArgsConstructor
@NoArgsConstructor
public class UrgentDonorRegistrationDTO {
    private Long bloodTypeId;
    private Double latitude;
    private Double longitude;
    private DonorReadinessLevel readinessLevel;
=======
public class UrgentDonorRegistrationDTO {
    private Long userId;
    private Long bloodTypeId;
    private AddressRequestDTO addressRequest; // Địa chỉ (City, District, Ward, etc.)
    private String location;
    private Double latitude;
    private Double longitude;

    // BỔ SUNG để service hoạt động đúng:
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String phone;
>>>>>>> origin/main
}
