package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotNull;
>>>>>>> origin/main
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabTestResultDTO {
<<<<<<< HEAD
    private Long labTestResultId;

=======
    @NotNull(message = "Lab test result ID không thể null")
    private Long labTestResultId;

    @NotNull(message = "Blood unit ID không thể null")
>>>>>>> origin/main
    private Long bloodUnitId;

    private boolean hivNegative;
    private boolean hbvNegative;
    private boolean hcvNegative;
    private boolean syphilisNegative;
    private boolean malariaNegative;

<<<<<<< HEAD
    private boolean passed;

    private LocalDateTime testedAt;

=======
    @NotNull(message = "Kết quả xét nghiệm (passed) không thể null")
    private boolean passed;

    @NotNull(message = "Ngày xét nghiệm không thể null")
    private LocalDateTime testedAt;

    @NotNull(message = "ID nhân viên xét nghiệm không thể null")
>>>>>>> origin/main
    private Long testedById;
    private String testedByName; // Optional: Hiển thị tên nhân viên xét nghiệm
}
