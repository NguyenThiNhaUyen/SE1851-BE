package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotNull;
>>>>>>> origin/main
import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
<<<<<<< HEAD
    private boolean enable;   // nên giữ là enable
    private Long roleId;

    // Bổ sung để nhận dữ liệu từ profile
    private String fullName;
    private String staffPosition;
}
=======
    private boolean enable;   // không nên dùng "isEnable" vì Lombok sẽ tạo `isIsEnable()` gây nhầm lẫn
    @NotNull(message = "Role ID không thể null")
    private Long roleId;
}

>>>>>>> origin/main
