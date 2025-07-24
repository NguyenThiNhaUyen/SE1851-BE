package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodUnitDTO {
    private Long bloodUnitId;
    private String unitCode;

    private Integer quantityMl;
    private LocalDate expirationDate;
    private String status;

    private LocalDateTime storedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long bloodTypeId;
    private String bloodTypeName;

    private Long componentId;
    private String componentName;

    private Long bloodBagId;
    private String bagCode;
}
