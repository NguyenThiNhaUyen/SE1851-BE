package com.quyet.superapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransfusionDTO {
    private Long id;

    private Long recipientId;
    private Long requestId;
    private Long bloodUnitId;

    private LocalDateTime transfusionDate;
    private String status;
    private String notes;
}
