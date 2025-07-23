package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
>>>>>>> origin/main
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
<<<<<<< HEAD
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    // 🔐 Thông tin đăng nhập
    private String username;
    private String email;
    private String password;

    // 📌 Vai trò (mặc định là MEMBER nếu không gửi)
    private String role;

    // 📄 Thông tin cá nhân để tạo UserProfile
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String citizenId;
    private String gender;
    private String phone;

    private AddressDTO address;

    // 💳 Thông tin bảo hiểm y tế
    private Boolean hasInsurance;              // Có BHYT không?
    private String insuranceCardNumber;        // Mã số thẻ
    private LocalDate insuranceValidTo;        // Ngày hết hạn
=======
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    // 🧑 Tài khoản người dùng
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;

    private String role; // mặc định là MEMBER nếu null

    // 📄 Hồ sơ cá nhân
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate dob;

    @NotBlank(message = "Giới tính không được để trống")
    private String gender;

    @NotBlank(message = "CCCD không được để trống")
    @Pattern(regexp = "\\d{12}", message = "CCCD phải gồm đúng 12 chữ số")
    private String cccd;

    private String occupation;

    @Positive(message = "Cân nặng phải là số dương")
    private Double weight;

    @Positive(message = "Chiều cao phải là số dương")
    private Double height;

    // ☎️ Thông tin liên hệ
    @Valid
    @NotNull(message = "Thông tin liên hệ không được để trống")
    private ContactInfoDTO contactInfo;

    // 🏠 Địa chỉ cư trú
    @Valid
    private AddressDTO address;

    // 🩺 Thông tin bảo hiểm
    private boolean hasInsurance;

    @Size(max = 20, message = "Số thẻ BHYT tối đa 20 ký tự")
    private String insuranceCardNumber;

    private LocalDate insuranceValidTo;
>>>>>>> origin/main
}
