package com.quyet.superapp.dto;

<<<<<<< HEAD
import com.quyet.superapp.entity.DonationRegistration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import com.quyet.superapp.enums.HealthCheckFailureReason;
import jakarta.validation.constraints.*;
import lombok.*;
>>>>>>> origin/main

import java.time.LocalDateTime;

@Data
<<<<<<< HEAD
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HealthCheckFailureLogDTO {

    private Long logId;
    private Long registrationId;
    private String reason;
    private String staffNote;
    private LocalDateTime createdAt;

=======
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
>>>>>>> origin/main
}
