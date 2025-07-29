package com.quyet.superapp.service;

import com.quyet.superapp.entity.Notification;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> getByUserId(Long userId) {
        return notificationRepository.findByUser_UserId(userId);
    }

    public List<Notification> getUnreadByUserId(Long userId) {
        return notificationRepository.findByUser_UserIdAndIsReadFalse(userId);
    }

    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> getById(Long id) {
        return notificationRepository.findById(id);
    }

    public Notification create(Notification notification) {
        if (notification.getContent() == null || notification.getContent().isBlank()) {
            throw new IllegalArgumentException("Nội dung thông báo không được để trống.");
        }

        notification.setSentAt(LocalDateTime.now());
        notification.setIsRead(false);

        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification update(Long id, Notification updatedNotification) {
        Notification existing = notificationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy thông báo với ID: " + id));

        if (updatedNotification.getContent() != null) {
            existing.setContent(updatedNotification.getContent());
        }

        if (updatedNotification.getIsRead() != null) {
            existing.setIsRead(updatedNotification.getIsRead());
        }

        // Không cho sửa sentAt hay user từ bên ngoài
        return notificationRepository.save(existing);
    }

    public void delete(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new NoSuchElementException("Không tồn tại thông báo với ID: " + id);
        }
        notificationRepository.deleteById(id);
    }

    public void sendEmergencyContact(User user, String message) {
        sendInAppNotification(user, message);
        sendEmail(user, message);
        sendSms(user, message);
    }

    private void sendInAppNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setContent(message);
        notification.setSentAt(LocalDateTime.now());
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }

    private void sendEmail(User user, String message) {
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            log.info("📧 Gửi email đến {}: {}", user.getEmail(), message);
        }
    }

    private void sendSms(User user, String message) {
        if (user.getUserProfile() != null && user.getUserProfile().getPhone() != null) {
            log.info("📱 Gửi SMS/Gọi đến {}: {}", user.getUserProfile().getPhone(), message);
        }
    }
}
