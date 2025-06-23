package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDate;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRegistrationDTO {

    private Long registrationId;          // ✅ phục vụ confirm/get
    private LocalDate scheduledDate;
    private String location;
    private String bloodType;
    private String status;                // ✅ trạng thái
     // ✅ thêm để hỗ trợ mapping từ entity

    // Thông tin người dùng (hiển thị ở bảng confirm)
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
