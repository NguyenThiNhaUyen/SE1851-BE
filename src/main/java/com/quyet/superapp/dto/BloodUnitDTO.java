package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodUnitDTO {
    private Long bloodUnitId;
    private Long bloodTypeId;
    private Long componentId;
    private Long donationId;
    private Integer quantityMl;
    private LocalDate expirationDate;
    private String status;
    private LocalDateTime storedAt;
}


