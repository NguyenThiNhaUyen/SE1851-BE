package com.quyet.superapp.dto;

import com.quyet.superapp.enums.HealthCheckFailureReason;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthCheckFailureLogDTO {

    @NotNull(message = "Log ID không thể null")
    private Long logId;

    @NotNull(message = "Registration ID không thể null")
    private Long registrationId;

    @NotNull(message = "Lý do không thể null")
    private HealthCheckFailureReason reason;

    private String staffNote;

    @NotNull(message = "Ngày tạo không thể null")
    private LocalDateTime createdAt;
}
