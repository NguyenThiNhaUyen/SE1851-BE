package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabTestResultDTO {
    @NotNull(message = "Lab test result ID không thể null")
    private Long labTestResultId;

    @NotNull(message = "Blood unit ID không thể null")
    private Long bloodUnitId;

    private boolean hivNegative;
    private boolean hbvNegative;
    private boolean hcvNegative;
    private boolean syphilisNegative;
    private boolean malariaNegative;

    @NotNull(message = "Kết quả xét nghiệm (passed) không thể null")
    private boolean passed;

    @NotNull(message = "Ngày xét nghiệm không thể null")
    private LocalDateTime testedAt;

    @NotNull(message = "ID nhân viên xét nghiệm không thể null")
    private Long testedById;
    private String testedByName; // Optional: Hiển thị tên nhân viên xét nghiệm
}
