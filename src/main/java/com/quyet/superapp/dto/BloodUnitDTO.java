package com.quyet.superapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodUnitDTO {
    private Long bloodUnitId;
    @NotNull(message = "ID nhóm máu không được để trống")
    private Long bloodTypeId;

    @NotNull(message = "ID thành phần máu không được để trống")
    private Long componentId;

    @NotNull(message = "ID túi máu không được để trống")
    private Long bloodBagId;

    @NotNull(message = "Thể tích không được để trống")
    @Min(value = 10, message = "Thể tích tối thiểu là 10ml")
    private Integer quantityMl;

    @NotNull(message = "Ngày hết hạn không được để trống")
    private LocalDate expirationDate;

    @NotBlank(message = "Trạng thái không được để trống")
    private String status;
    private LocalDateTime storedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String unitCode;         // để truy xuất hoặc in tem
    private String bloodTypeName;    // để hiển thị rõ hơn
    private String componentName;    // để hiển thị rõ hơn
}


