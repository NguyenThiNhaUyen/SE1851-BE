package com.quyet.superapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveBloodRequestDTO {
<<<<<<< HEAD
    private Long bloodRequestId; // ✅ đổi tên
    private String status;
=======
    private Long bloodRequestId;
    private String status;                 // APPROVED, PARTIALLY_APPROVED, REJECTED
>>>>>>> origin/main
    private Integer confirmedVolumeMl;
    private Boolean isUnmatched;
    private String emergencyNote;
    private String approvedBy;
}
<<<<<<< HEAD

=======
>>>>>>> origin/main
