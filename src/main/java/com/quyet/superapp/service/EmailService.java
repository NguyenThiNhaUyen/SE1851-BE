package com.quyet.superapp.service;

import com.quyet.superapp.entity.EmailLog;
import com.quyet.superapp.entity.User;

import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.repository.EmailLogRepository;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;




@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;


    /**
     * ✅ Gửi email bất kỳ và lưu log
     */
    public void sendEmail(User user, String subject, String content, String type) {
        EmailLog emailLog = new EmailLog();
        emailLog.setUser(user);
        emailLog.setRecipientEmail(user.getEmail());
        emailLog.setSubject(subject);
        emailLog.setBody(content);
        emailLog.setType(type);
        emailLog.setSentAt(LocalDateTime.now());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true); // HTML enabled
            mailSender.send(message);

            emailLog.setStatus("SUCCESS");
            log.info("📧 Email gửi thành công đến: {}", user.getEmail());
        } catch (Exception e) {
            emailLog.setStatus("FAILED");
            log.error("❌ Gửi email thất bại đến {}: {}", user.getEmail(), e.getMessage(), e);
        }

        emailLogRepository.save(emailLog);
    }
    public void sendRecoveryReminder(User user, BloodComponentType component, LocalDate nextEligibleDate) {
        String componentName = switch (component) {
            case PLASMA -> "Huyết tương (Plasma)";
            case RBC -> "Hồng cầu (RBC)";
            case PLATELET -> "Tiểu cầu (Platelet)";
            case WBC -> "Bạch cầu (WBC)";

        };

        String content = "<p>Xin chào <b>" + user.getUsername() + "</b>,</p>" +
                "<p>Bạn đã từng hiến " + componentName + " cách đây một thời gian.</p>" +
                "<p>Hệ thống xin thông báo rằng bạn sẽ đủ điều kiện hiến lại vào ngày: <b>"
                + nextEligibleDate.toString() + "</b>.</p>" +
                "<p>Hãy đảm bảo sức khỏe và quay lại tham gia hiến máu nhé! ❤️</p>" +
                "<br><p>Trân trọng,</p><p>Hệ thống hỗ trợ hiến máu</p>";

        sendEmail(user, "Nhắc nhở phục hồi hiến máu", content, "REMINDER");
    }

    /**
     * ✅ Gửi email chứa OTP đăng ký
     */
    public void sendRegisterOtp(User user, String otp) {
        String content = "<p>Xin chào <b>" + user.getUsername() + "</b>,</p>" +
                "<p>Mã OTP xác thực tài khoản của bạn là: <b style='font-size: 18px;'>" + otp + "</b></p>" +
                "<p>Vui lòng nhập mã này trong vòng <b>5 phút</b> để hoàn tất đăng ký.</p>" +
                "<br><p>Trân trọng,</p><p>Hệ thống hỗ trợ hiến máu</p>";

        sendEmail(user, "Mã xác thực OTP đăng ký tài khoản", content, "OTP_REGISTER");
    }


}
