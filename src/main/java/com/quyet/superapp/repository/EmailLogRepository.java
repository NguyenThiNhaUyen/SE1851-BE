package com.quyet.superapp.repository;

import com.quyet.superapp.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.time.LocalDateTime;
=======
>>>>>>> origin/main
import java.util.List;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
<<<<<<< HEAD

    List<EmailLog> findByUser_UserId(Long userId);

    List<EmailLog> findByStatusIgnoreCase(String status);

    List<EmailLog> findByTypeIgnoreCase(String type);

    List<EmailLog> findBySentAtBetween(LocalDateTime start, LocalDateTime end);
=======
    List<EmailLog> findByUser_UserId(Long userId);
>>>>>>> origin/main
}
