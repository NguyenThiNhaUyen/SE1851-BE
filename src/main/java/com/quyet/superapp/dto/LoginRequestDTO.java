package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Tên đăng nhập không thể trống")
    private String username;

    @NotBlank(message = "Mật khẩu không thể trống")
    private String password;

}
