package com.quyet.superapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodRequestDTO {
    private Long bloodRequestId;
    @NotNull(message = "Người yêu cầu không được để trống")
    private Long requesterId;

    @NotNull(message = "Nhóm máu không được để trống")
    private Long bloodTypeId;

    @NotNull(message = "Thành phần máu không được để trống")
    private Long componentId;

    @NotNull(message = "Số lượng máu yêu cầu không được để trống")
    @Min(value = 50, message = "Số lượng yêu cầu phải >= 50ml")
    private Integer quantityMl;

    @NotBlank(message = "Mức độ khẩn cấp không được để trống")
    private String urgencyLevel;

    private String status;

    private LocalDateTime createdAt;

    @Min(value = 0, message = "Lượng xác nhận phải >= 0ml")
    private Integer confirmedVolumeMl;

    private String reason;                 // Lý do truyền máu
    private Boolean isUnmatched;          // true nếu dùng máu O- chưa định nhóm
    private String triageLevel;           // RED, YELLOW, BLACK (cho Code Red)
    private Long codeRedId;               // Mã sự kiện Code Red nếu có
    private String emergencyNote;         // Ghi chú nếu là cấp cứu
    private String approvedBy;            // Tên admin duyệt
    private LocalDateTime approvedAt;     // Thời gian duyệt


}

