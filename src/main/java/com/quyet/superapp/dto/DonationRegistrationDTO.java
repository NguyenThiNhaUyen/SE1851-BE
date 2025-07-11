package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRegistrationDTO {
    private Long registrationId;
    private LocalDateTime scheduledDate;
    private String location;

    private Long bloodTypeId;               // ✅ sửa thành ID
    private String bloodTypeDescription;    // ✅ nếu cần hiển thị tên nhóm máu
    private String status;

    // Thông tin người dùng
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String phone;
    private Long addressId;
    private String addressFull;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String email;
}
