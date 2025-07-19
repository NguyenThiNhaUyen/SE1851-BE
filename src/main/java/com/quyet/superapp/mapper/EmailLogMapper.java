package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.EmailLogDTO;
import com.quyet.superapp.entity.EmailLog;

public class EmailLogMapper {

    public static EmailLogDTO toDTO(EmailLog log) {
        EmailLogDTO dto = new EmailLogDTO();
        dto.setEmailId(log.getId());
        dto.setUserId(log.getUser() != null ? log.getUser().getUserId() : null);
        dto.setUsername(log.getUser() != null ? log.getUser().getUsername() : null);
        dto.setRecipientEmail(log.getRecipientEmail());
        dto.setSubject(log.getSubject());
        dto.setBody(log.getBody());
        dto.setType(log.getType());
        dto.setSentAt(log.getSentAt());
        dto.setStatus(log.getStatus());
        return dto;
    }
}
