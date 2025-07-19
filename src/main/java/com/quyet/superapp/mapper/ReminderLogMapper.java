package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.ReminderLogDTO;
import com.quyet.superapp.entity.ReminderLog;
import com.quyet.superapp.entity.User;

public class ReminderLogMapper {

    public static ReminderLogDTO toDTO(ReminderLog log) {
        return ReminderLogDTO.builder()
                .reminderLogId(log.getReminderLogId())
                .userId(log.getUser().getUserId())
                .bloodComponent(log.getBloodComponent())
                .nextEligibleDate(log.getNextEligibleDate())
                .reminderSentAt(log.getReminderSentAt())
                .note(log.getNote())
                .build();
    }

    public static ReminderLog toEntity(ReminderLogDTO dto, User user) {
        return ReminderLog.builder()
                .reminderLogId(dto.getReminderLogId())
                .user(user)
                .bloodComponent(dto.getBloodComponent())
                .nextEligibleDate(dto.getNextEligibleDate())
                .reminderSentAt(dto.getReminderSentAt())
                .note(dto.getNote())
                .build();
    }
}
