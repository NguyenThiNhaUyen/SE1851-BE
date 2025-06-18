package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodRequestDTO {
    private Long bloodRequestId;
    private Long requesterId;
    private Long bloodTypeId;
    private Long componentId;
    private Integer quantityMl;
    private String urgencyLevel;
    private String status;
    private LocalDateTime createdAt;
    private Integer confirmedVolumeMl;


}

