package com.quyet.superapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodInventoryDTO {
    @NotNull(message = "Không được để trống")
    private Long bloodInventoryId;

    @NotNull(message = "Không được để trống")
    private Long bloodTypeId;

    @NotBlank(message = "Không được để trống")
    private String bloodTypeName;

    @NotNull(message = "Không được để trống")
    private Long componentId;

    @NotBlank(message = "Không được để trống")
    private String componentName;

    @Min(value = 0, message = "Giá trị phải >= 0")
    private Integer totalQuantityMl;

    @Min(value = 0, message = "Giá trị phải >= 0")
    private Integer standardBagSize;    // ✅ size 250ml, 350ml, 450ml

    @Min(value = 0, message = "Giá trị phải >= 0")
    private Integer estimatedBags;      // ✅ tính từ totalQuantityMl / standardBagSize
    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


