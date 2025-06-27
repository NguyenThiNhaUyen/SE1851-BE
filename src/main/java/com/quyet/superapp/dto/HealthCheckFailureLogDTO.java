package com.quyet.superapp.dto;

import com.quyet.superapp.entity.DonationRegistration;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HealthCheckFailureLogDTO {

    @NotNull(message = "Log ID không thể null")
    private Long logId;

    @NotNull(message = "Registration ID không thể null")
    private Long registrationId;

    @NotBlank(message = "Lý do không thể trống")
    private String reason;

    private String staffNote;

    @NotNull(message = "Ngày tạo không thể null")
    private LocalDateTime createdAt;

}
