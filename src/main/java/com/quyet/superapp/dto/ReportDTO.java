package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
>>>>>>> origin/main
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDTO {
<<<<<<< HEAD
    private Long id;
    private String reportType;
    private Long generatedById;
    private LocalDateTime createdAt;
=======
    @NotNull(message = "ID báo cáo không thể null")
    private Long id;

    @NotBlank(message = "Loại báo cáo không thể trống")
    private String reportType;

    @NotNull(message = "ID người tạo báo cáo không thể null")
    private Long generatedById;

    @NotNull(message = "Ngày tạo không thể null")
    private LocalDateTime createdAt;

    @NotBlank(message = "Nội dung báo cáo không thể trống")
>>>>>>> origin/main
    private String content;
}
