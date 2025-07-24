package com.quyet.superapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveBloodRequestDTO {
    private Long bloodRequestId;
    private String status;                 // APPROVED, PARTIALLY_APPROVED, REJECTED
    private Integer confirmedVolumeMl;
    private Boolean isUnmatched;
    private String emergencyNote;
    private String approvedBy;
}
