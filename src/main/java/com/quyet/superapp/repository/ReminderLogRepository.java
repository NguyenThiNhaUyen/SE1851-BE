package com.quyet.superapp.repository;

import com.quyet.superapp.entity.ReminderLog;
import com.quyet.superapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderLogRepository extends JpaRepository<ReminderLog, Long> {


    boolean existsByUserAndReminderSentAtBetween(User user, LocalDateTime start, LocalDateTime end);

}
