package com.quyet.superapp.dto;

import com.quyet.superapp.dto.ContactInfoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    // 🔐 Thông tin tài khoản
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
    private String role; // mặc định: MEMBER
    // 📄 Hồ sơ cá nhân
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;
    private LocalDate dob;
    @NotBlank(message = "Giới tính không được để trống")
    private String gender;
    @Valid
    private ContactInfoDTO contactInfo;
    @NotBlank(message = "CCCD không được để trống")
    @Pattern(regexp = "\\d{12}", message = "CCCD phải gồm đúng 12 chữ số")
    private String cccd;
    private String occupation;
    @Positive(message = "Cân nặng phải là số dương")
    private Double weight;
    @Positive(message = "Chiều cao phải là số dương")
    private Double height;
    @Valid
    private AddressDTO address;
}
