    package com.quyet.superapp.service;
    
    import com.quyet.superapp.entity.EmailLog;
    import com.quyet.superapp.entity.User;
    
    import com.quyet.superapp.enums.BloodComponentType;
    import com.quyet.superapp.enums.EmailType;
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
                helper.setText(content, true); // HTML
                mailSender.send(message);
    
                emailLog.setStatus("SUCCESS");
                log.info("📧 Email gửi thành công đến: {}", user.getEmail());
            } catch (Exception e) {
                emailLog.setStatus("FAILED");
                log.error("❌ Gửi email thất bại đến {}: {}", user.getEmail(), e.getMessage(), e);
            }
    
            emailLogRepository.save(emailLog);
        }
    
        // ✅ Gửi pre-reminder
        public void sendPreRecoveryReminder(User user, BloodComponentType component, LocalDate nextEligibleDate) {
            String componentName = switch (component) {
                case PLASMA -> "Huyết tương (Plasma)";
                case RBC -> "Hồng cầu (RBC)";
                case PLATELET -> "Tiểu cầu (Platelet)";
                case WBC -> "Bạch cầu (WBC)";
            };
    
            String content = "<p>Xin chào <b>" + user.getUsername() + "</b>,</p>" +
                    "<p>Bạn đã từng hiến " + componentName + " và đang trong giai đoạn phục hồi.</p>" +
                    "<p>Chỉ còn vài ngày nữa, bạn sẽ đủ điều kiện để tiếp tục hiến máu: <b>"
                    + nextEligibleDate + "</b>.</p>" +
                    "<p>Hãy chuẩn bị sức khỏe thật tốt để quay lại cùng cộng đồng nhé! 💪</p>" +
                    "<br><p>Trân trọng,</p><p>Hệ thống hỗ trợ hiến máu</p>";
    
            sendEmail(user, "Sắp đủ điều kiện hiến máu lại", content, "PRE_REMINDER");
        }
    
        // ✅ Gửi reminder khi đã đủ điều kiện
        public void sendRecoveryReminder(User user, BloodComponentType component, LocalDate nextEligibleDate) {
            String componentName = switch (component) {
                case PLASMA -> "Huyết tương (Plasma)";
                case RBC -> "Hồng cầu (RBC)";
                case PLATELET -> "Tiểu cầu (Platelet)";
                case WBC -> "Bạch cầu (WBC)";
            };
    
            String content = "<p>Xin chào <b>" + user.getUsername() + "</b>,</p>" +
                    "<p>Hệ thống xin thông báo rằng bạn đã đủ điều kiện để hiến lại " + componentName + " kể từ ngày: <b>"
                    + nextEligibleDate + "</b>.</p>" +
                    "<p>Hãy đăng ký lịch hiến máu để tiếp tục lan tỏa yêu thương! ❤️</p>" +
                    "<br><p>Trân trọng,</p><p>Hệ thống hỗ trợ hiến máu</p>";
    
            sendEmail(user, "Đã đủ điều kiện hiến máu", content, "REMINDER");
        }
        public void sendPreDonationInstructionEmail(User user, LocalDate scheduledDate) {
            String content = "<h3>Xin chào " + user.getUsername() + "</h3>" +
                    "<p>Chúng tôi xác nhận bạn đã đăng ký hiến máu vào ngày <b>" + scheduledDate + "</b>.</p>" +
                    "<p>Vui lòng chuẩn bị tốt trước khi hiến máu:</p>" +
                    "<ul>" +
                    "<li>Ngủ đủ giấc và ăn nhẹ trước hiến máu</li>" +
                    "<li>Tránh đồ ăn dầu mỡ hoặc rượu bia</li>" +
                    "<li>Mang theo CMND/CCCD</li>" +
                    "</ul>" +
                    "<p>Trân trọng,</p><p>Hệ thống hỗ trợ hiến máu</p>";

            sendEmail(user, "Hướng dẫn trước khi hiến máu", content, "HƯỚNG DẪN HIẾN MÁU");
        }



    }
