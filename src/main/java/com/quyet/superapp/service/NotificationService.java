package com.quyet.superapp.service;

import com.quyet.superapp.entity.Notification;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> getByUserId(Long userId) {
        return notificationRepository.findByUser_UserId(userId);
    }

    public List<Notification> getUnreadByUserId(Long userId) {
        return notificationRepository.findByUser_UserIdAndIsReadFalse(userId);
    }

    // ✅ Lấy tất cả thông báo (admin có thể dùng)
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    // ✅ Lấy thông báo theo ID
    public Optional<Notification> getById(Long id) {
        return notificationRepository.findById(id);
    }

    // ✅ Tạo mới 1 thông báo (sử dụng cho in-app notification)
    public Notification create(Notification notification) {
        if (notification.getSentAt() == null) {
            notification.setSentAt(LocalDateTime.now());
        }
        notification.setIsRead(false);
        return notificationRepository.save(notification);
    }

    // ✅ Cập nhật thông báo (nội dung, thời gian, người nhận, trạng thái đã đọc)
    public Notification update(Long id, Notification updatedNotification) {
        return notificationRepository.findById(id)
                .map(existing -> {
                    existing.setContent(updatedNotification.getContent());
                    existing.setSentAt(updatedNotification.getSentAt());
                    existing.setIsRead(updatedNotification.getIsRead());
                    existing.setUser(updatedNotification.getUser());
                    return notificationRepository.save(existing);
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
}
