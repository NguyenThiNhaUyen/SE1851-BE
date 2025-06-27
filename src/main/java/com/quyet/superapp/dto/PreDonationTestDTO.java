package com.quyet.superapp.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreDonationTestDTO {
    @NotNull(message = "Pre-donation test ID không thể null")
    private Long preDonationTestId;

    @NotNull(message = "Health check ID không thể null")
    private Long healthCheckId;
    private Boolean hivResult;
    private Boolean hbvResult;
    private Boolean hcvResult;
    private Boolean syphilisResult;
    @DecimalMin(value = "0.0", message = "Mức độ HB phải là một số dương")
    @DecimalMax(value = "25.0", message = "Mức độ HB không được quá 25 g/dL")
    private Double hbLevel;

    @NotNull(message = "Blood type ID không thể null")
    private Long bloodTypeId;

    @NotNull(message = "Ngày xét nghiệm không thể null")
    private LocalDate testDate;
}
