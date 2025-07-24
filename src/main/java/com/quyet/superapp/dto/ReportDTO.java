package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDTO {
    @NotNull(message = "ID báo cáo không thể null")
    private Long id;

    @NotBlank(message = "Loại báo cáo không thể trống")
    private String reportType;

    @NotNull(message = "ID người tạo báo cáo không thể null")
    private Long generatedById;

    @NotNull(message = "Ngày tạo không thể null")
    private LocalDateTime createdAt;

    @NotBlank(message = "Nội dung báo cáo không thể trống")
    private String content;
}
