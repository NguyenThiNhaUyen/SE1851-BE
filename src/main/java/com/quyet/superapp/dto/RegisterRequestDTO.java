package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
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
    private String cccd;
    private String gender;
    private String phone;
    private AddressDTO address;


}
