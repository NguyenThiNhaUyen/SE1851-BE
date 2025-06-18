package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRegistrationDTO {
    private Long registrationId;          // ✅ thêm để phục vụ confirm/get
    private LocalDate scheduledDate;
    private String location;
    private String bloodType;
    private String status;                // ✅ thêm trạng thái

    // Thông tin người dùng (chỉ dùng khi POST nếu chưa có hồ sơ)
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String phone;
    private Long addressId;
    private String addressFull;
}

