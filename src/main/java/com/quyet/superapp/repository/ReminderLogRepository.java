package com.quyet.superapp.repository;

import com.quyet.superapp.entity.ReminderLog;
import com.quyet.superapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderLogRepository extends JpaRepository<ReminderLog, Long> {

    List<ReminderLog> findByUser_UserId(Long userId);
    boolean existsByUserAndReminderSentAtBetween(User user, LocalDateTime start, LocalDateTime end);

}
