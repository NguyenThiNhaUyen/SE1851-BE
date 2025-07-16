package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailLogDTO {
    private Long EmailId;
    private Long userId;
    private String username;
    private String recipientEmail;
    private String subject;
    private String body;
    private String type;
    private LocalDateTime sentAt;
    private String status;

}
