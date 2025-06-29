package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRegistrationDTO {

    private Long registrationId;          // ✅ thêm để phục vụ confirm/get
    @NotNull(message = "Thời gian hẹn không được để trống")
    private LocalDate scheduledDate;

    @NotBlank(message = "Địa điểm không được để trống")
    private String location;

    @NotBlank(message = "Nhóm máu không được để trống")
    private String bloodType;

    private String status;

    // Các thông tin nhập liệu nếu chưa có hồ sơ
    @NotBlank(message = "Họ tên không được để trống")

    private String fullName;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate dob;

    @NotBlank(message = "Giới tính không được để trống")
    private String gender;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{9,12}$", message = "Số điện thoại không hợp lệ")
    private String phone;
    private Long addressId;
    private String addressFull;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String email;

}
