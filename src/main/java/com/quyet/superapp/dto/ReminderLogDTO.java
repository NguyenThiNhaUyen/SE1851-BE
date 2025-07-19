package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderLogDTO {
    private Long reminderLogId;
    private Long userId;
    private String bloodComponent;
    private LocalDateTime nextEligibleDate;
    private LocalDateTime reminderSentAt;
    private String note;
}
