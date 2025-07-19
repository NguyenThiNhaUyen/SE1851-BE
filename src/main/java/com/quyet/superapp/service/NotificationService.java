package com.quyet.superapp.service;

import com.quyet.superapp.entity.Notification;
import com.quyet.superapp.entity.User;
<<<<<<< HEAD
import com.quyet.superapp.enums.DonorReadinessLevel;
import com.quyet.superapp.repository.NotificationRepository;
import com.quyet.superapp.repository.UrgentDonorRegistryRepository;
import com.quyet.superapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
=======
import com.quyet.superapp.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
>>>>>>> origin/main

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
<<<<<<< HEAD
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UrgentDonorRegistryRepository urgentDonorRegistryRepository;

    private final EmailService emailService; // thêm vào constructor

    private void sendEmail(User user, String message) {
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            emailService.sendEmail(user, "🔔 Thông báo từ hệ thống", message, "SYSTEM_NOTIFICATION");
        }
    }


    @Transactional
    public void sendNotificationToRole(String roleName, String title, String message) {
        List<User> users = userRepository.findByRoleName(roleName);
        for (User user : users) {
            sendEmergencyContact(user, title + ": " + message);
        }
    }

    /**
     * ✅ Gửi thông báo đơn giản cho một user theo ID
     */
    @Transactional
    public void sendNotification(Long userId, String message, String redirectUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy userId: " + userId));

        // Nội dung thông báo kèm đường dẫn
        String content = message;
        if (redirectUrl != null && !redirectUrl.isBlank()) {
            content += " → " + redirectUrl;
        }

        sendInAppNotification(user, content);
    }



    @Transactional
    public void sendNotificationToReadinessGroup(DonorReadinessLevel level, String title, String message) {
        List<User> users = urgentDonorRegistryRepository.findUrgentDonorsByLevel(level);
        for (User user : users) {
            sendEmergencyContact(user, title + ": " + message);
        }
    }


    /**
     * ✅ Gửi thông báo in-app cho một user cụ thể
     */
    public Notification createNotification(Long userId, String title, String content) {
        Notification notification = new Notification();
        notification.setUser(new User(userId));
        notification.setContent(title + ": " + content);
        notification.setIsRead(false);
        notification.setSentAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    /**
     * ✅ Gửi thông báo khẩn cấp qua in-app + email + SMS
     */
    @Transactional
    public void sendEmergencyContact(User user, String message) {
        sendInAppNotification(user, message);
        sendEmail(user, message);
        sendSMS(user, message);
    }

    private void sendInAppNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setContent(message);
        notification.setSentAt(LocalDateTime.now());
        notification.setIsRead(false);
        notificationRepository.save(notification);

        log.info("🔔 Gửi in-app notification đến user {}: {}", user.getUsername(), message);
    }


    private void sendSMS(User user, String message) {
        if (user.getUserProfile() != null && user.getUserProfile().getPhone() != null) {
            log.info("📱 Gửi SMS đến {}: {}", user.getUserProfile().getPhone(), message);
            // TODO: Tích hợp SMS Provider (Twilio, Viettel, etc.)
        }
    }

    /**
     * ✅ Gửi cảnh báo hệ thống cho toàn bộ admin
     */
    @Transactional
    public void sendSystemAlertToAdmins(String message) {
        List<User> admins = userRepository.findByRoleName("ADMIN");
        for (User admin : admins) {
            sendEmergencyContact(admin, message);
        }
    }

    // 📦 Gửi cảnh báo tồn kho máu qua email
    public void sendInventoryAlertEmail(String message) {
        List<User> admins = userRepository.findByRoleName("ADMIN");
        for (User admin : admins) {
            if (admin.getEmail() != null && !admin.getEmail().isBlank()) {
                log.warn("📧 [ALERT] Gửi email cho admin {}: {}", admin.getEmail(), message);
                // TODO: Gửi thật bằng EmailService nếu cần
            }
        }
    }

    // 📦 Gửi cảnh báo tồn kho máu qua thông báo in-app
    public void sendInventoryAlertNotification(String message) {
        List<User> admins = userRepository.findByRoleName("ADMIN");
        for (User admin : admins) {
            createNotification(admin.getUserId(), "📦 Cảnh báo kho máu", message);
        }
    }

    // 📥 Truy xuất thông báo
=======
public class NotificationService {

    private final NotificationRepository notificationRepository;

>>>>>>> origin/main
    public List<Notification> getByUserId(Long userId) {
        return notificationRepository.findByUser_UserId(userId);
    }

    public List<Notification> getUnreadByUserId(Long userId) {
        return notificationRepository.findByUser_UserIdAndIsReadFalse(userId);
    }

<<<<<<< HEAD
=======
    // ✅ Lấy tất cả thông báo (admin có thể dùng)
>>>>>>> origin/main
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

<<<<<<< HEAD
=======
    // ✅ Lấy thông báo theo ID
>>>>>>> origin/main
    public Optional<Notification> getById(Long id) {
        return notificationRepository.findById(id);
    }

<<<<<<< HEAD
    // ✏️ Tạo hoặc cập nhật thủ công
=======
    // ✅ Tạo mới 1 thông báo (sử dụng cho in-app notification)
>>>>>>> origin/main
    public Notification create(Notification notification) {
        if (notification.getSentAt() == null) {
            notification.setSentAt(LocalDateTime.now());
        }
        notification.setIsRead(false);
        return notificationRepository.save(notification);
    }

<<<<<<< HEAD
=======
    // ✅ Cập nhật thông báo (nội dung, thời gian, người nhận, trạng thái đã đọc)
>>>>>>> origin/main
    public Notification update(Long id, Notification updatedNotification) {
        return notificationRepository.findById(id)
                .map(existing -> {
                    existing.setContent(updatedNotification.getContent());
                    existing.setSentAt(updatedNotification.getSentAt());
                    existing.setIsRead(updatedNotification.getIsRead());
                    existing.setUser(updatedNotification.getUser());
                    return notificationRepository.save(existing);
<<<<<<< HEAD
                }).orElse(null);
    }

    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
=======
                })
                .orElse(null);
    }

    // ✅ Xoá thông báo theo ID
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    // ✅ Gửi thông báo khẩn cấp qua 3 kênh: app + email + SMS giả lập
    public void sendEmergencyContact(User user, String message) {
        // Gửi in-app notification
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setContent(message);
        notification.setSentAt(LocalDateTime.now());
        notification.setIsRead(false);
        notificationRepository.save(notification);

        // Gửi email nếu có
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            System.out.println("📧 Gửi email đến " + user.getEmail() + ": " + message);
        }

        // Gửi SMS nếu có số điện thoại
        if (user.getUserProfile() != null && user.getUserProfile().getPhone() != null) {
            System.out.println("📱 Gửi SMS/Gọi đến " + user.getUserProfile().getPhone() + ": " + message);
        }
    }
>>>>>>> origin/main
}
