package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotBlank;
>>>>>>> origin/main
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
<<<<<<< HEAD
    private String username;
=======
    @NotBlank(message = "Tên đăng nhập không thể trống")
    private String username;

    @NotBlank(message = "Mật khẩu không thể trống")
>>>>>>> origin/main
    private String password;

}
