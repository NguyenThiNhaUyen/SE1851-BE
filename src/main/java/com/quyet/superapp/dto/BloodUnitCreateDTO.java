package com.quyet.superapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodUnitCreateDTO {

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
}
