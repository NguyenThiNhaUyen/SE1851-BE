package com.quyet.superapp.dto;

import com.quyet.superapp.enums.RequestStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UrgentRequestDTO {
    private Long urgentRequestId;
    private String hospitalName;
    private String bloodType;
    private int units;
    private LocalDate requestDate;
    private RequestStatus status;
    private Long requesterId;
}

