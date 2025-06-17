package com.quyet.superapp.service;

import com.quyet.superapp.entity.EmailLog;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.repository.EmailLogRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    public void sendEmail(User user, String subject, String content, String type) {
        EmailLog log = new EmailLog();
        log.setUser(user);
        log.setRecipientEmail(user.getEmail());
        log.setSubject(subject);
        log.setBody(content);
        log.setType(type);
        log.setSentAt(LocalDateTime.now());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);

            log.setStatus("SUCCESS");
        } catch (Exception e) {
            log.setStatus("FAILED");
        }

        emailLogRepository.save(log);
    }

}
