package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationHistoryDTO {
    private LocalDate donationDate;
    private String location;
    private Integer volumeMl;
    private String bloodGroup;
    private String component;
    private String status;

    // ✅ Thêm 2 trường mới để hiển thị thời gian phục hồi
    private LocalDate recoveryDate;     // Ngày có thể hiến lại
    private boolean isRecovered;        // Đã đủ điều kiện hiến lại?
}
