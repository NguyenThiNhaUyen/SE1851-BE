package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ReminderLogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Reminder_Log_Id")
    private Long reminderLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;

    @Column(name = "Blood_Component", nullable = false)
    private String bloodComponent; // Lưu dạng chuỗi cho đơn giản, hoặc dùng Enum

    @Column(name = "Next_Eligible_Date", nullable = false)
    private LocalDateTime nextEligibleDate;

    @Column(name = "Reminder_Sent_At", nullable = false)
    private LocalDateTime reminderSentAt;

    @Column(name = "Note")
    private String note;
}
