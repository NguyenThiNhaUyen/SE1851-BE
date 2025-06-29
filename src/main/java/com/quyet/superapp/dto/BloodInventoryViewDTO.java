package com.quyet.superapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BloodInventoryViewDTO {
    @NotNull(message = "Không được để trống")
    private Long id;

    @NotBlank(message = "Không được để trống")
    private String bloodType;

    @NotBlank(message = "Không được để trống")
    private String componentName;

    @Min(value = 0, message = "Giá trị phải >= 0")
    private Integer quantity;
    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
