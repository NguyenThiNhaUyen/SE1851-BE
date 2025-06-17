package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    //Thông tin tài khoản
    private String username;
    private String email;
    private String password;

    // Thông tin chi tiết
    private String firstName;
    private String lastName;
    private String phone;
    private AddressDTO address;
    private LocalDate dob;
    private String gender;
    private String role;
}
