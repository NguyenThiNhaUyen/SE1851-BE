package com.quyet.superapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodSeparationSuggestionDTO {
    @Min(value = 0, message = "Thể tích hồng cầu phải >= 0")
    private int redCellsMl;

    @Min(value = 0, message = "Thể tích huyết tương phải >= 0")
    private int plasmaMl;

    @Min(value = 0, message = "Thể tích tiểu cầu phải >= 0")
    private int plateletsMl;

    @NotBlank(message = "Nhãn hồng cầu không được để trống")
    private String redCellLabel;

    @NotBlank(message = "Nhãn huyết tương không được để trống")
    private String plasmaLabel;

    @NotBlank(message = "Nhãn tiểu cầu không được để trống")
    private String plateletsLabel;

    private String note;
}
