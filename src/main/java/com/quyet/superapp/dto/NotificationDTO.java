package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    @NotNull(message = "Notification ID không thể null")
    private Long notificationId;

    @NotNull(message = "User ID không thể null")
    private Long userId;

    @NotBlank(message = "Nội dung thông báo không thể trống")
    private String content;

    @NotNull(message = "Ngày gửi không thể null")
    private LocalDateTime sentAt;

    private Boolean isRead; // ✅ đổi tên cho đồng nhất với entity

    @NotNull(message = "Ngày tạo không thể null")
    private LocalDateTime createdAt;

    @NotNull(message = "Ngày cập nhật không thể null")
    private LocalDateTime updatedAt;
}
