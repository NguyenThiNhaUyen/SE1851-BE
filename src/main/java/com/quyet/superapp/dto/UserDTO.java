package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private boolean enable;   // không nên dùng "isEnable" vì Lombok sẽ tạo `isIsEnable()` gây nhầm lẫn
    @NotNull(message = "Role ID không thể null")
    private Long roleId;
}

