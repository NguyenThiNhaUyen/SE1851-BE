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

    private String reason;                 // Lý do truyền máu
    private Boolean isUnmatched;          // true nếu dùng máu O- chưa định nhóm
    private String triageLevel;           // RED, YELLOW, BLACK (cho Code Red)
    private Long codeRedId;               // Mã sự kiện Code Red nếu có
    private String emergencyNote;         // Ghi chú nếu là cấp cứu
    private String approvedBy;            // Tên admin duyệt
    private LocalDateTime approvedAt;     // Thời gian duyệt


}

