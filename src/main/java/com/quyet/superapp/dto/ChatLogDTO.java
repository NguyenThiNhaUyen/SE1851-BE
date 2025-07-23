package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
>>>>>>> origin/main
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatLogDTO {
    private Long chatId;
<<<<<<< HEAD
    private Long userId;
    private String message;
=======
    @NotNull(message = "ID người dùng không được để trống")
    private Long userId;

    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    @Size(max = 1000, message = "Nội dung không được vượt quá 1000 ký tự")
    private String message;

    @NotBlank(message = "Người gửi không được để trống")
>>>>>>> origin/main
    private String sender;
    private LocalDateTime createdAt;
}
