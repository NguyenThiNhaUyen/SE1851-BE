package com.quyet.superapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodSeparationRequestDTO {
    @NotNull(message = "donationId không được để trống")
    private Long donationId;

    @Min(value = 10, message = "Thể tích hồng cầu phải >= 10ml")
    private int redCellsMl;

    @Min(value = 10, message = "Thể tích huyết tương phải >= 10ml")
    private int plasmaMl;

    @Min(value = 10, message = "Thể tích tiểu cầu phải >= 10ml")
    private int plateletsMl;

    @NotBlank(message = "Phương pháp tách máu không được để trống")
    private String method;

    private boolean leukoreduced;

    @NotNull(message = "staffId không được để trống")
    private Long staffId; // ID của nhân viên đang thực hiện
}
