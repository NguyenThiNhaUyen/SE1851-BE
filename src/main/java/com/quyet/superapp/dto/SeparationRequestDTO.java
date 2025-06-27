package com.quyet.superapp.dto;

import com.quyet.superapp.enums.SeparationMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeparationRequestDTO {
    @NotNull(message = "Blood Bag ID không thể null")
    private Long bloodBagId;

    @NotNull(message = "Phương pháp tách máu không thể null")
    private SeparationMethod separationMethod;

    @NotBlank(message = "Tên hoặc ID nhân viên không thể trống")
    private String operator; // ID hoặc tên nhân viên

}
