package com.quyet.superapp.controller;

import com.quyet.superapp.entity.Notification;
import com.quyet.superapp.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;


import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
<<<<<<< HEAD
=======
@Validated
>>>>>>> origin/main
public class NotificationController {

    private final NotificationService service;

<<<<<<< HEAD
    // 📬 Gửi thông báo đến user cụ thể từ API
    @PostMapping("/send-to-user/{userId}")
    public ResponseEntity<String> sendToUser(
            @PathVariable Long userId,
            @RequestParam String message,
            @RequestParam(required = false) String redirectUrl) {
        service.sendNotification(userId, message, redirectUrl);
        return ResponseEntity.ok("Đã gửi thông báo đến user " + userId);
    }

    @PutMapping("/mark-as-read/{id}")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {
        Notification n = service.getById(id).orElse(null);
        if (n == null) return ResponseEntity.notFound().build();

        n.setIsRead(true);
        service.update(id, n);
        return ResponseEntity.ok("Đã đánh dấu là đã đọc");
    }


=======
>>>>>>> origin/main
    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

<<<<<<< HEAD
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getById(@PathVariable Long id) {
=======
    @GetMapping("/by-id")
    public ResponseEntity<Notification> getById(@RequestParam Long id) {
>>>>>>> origin/main
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Lấy tất cả thông báo theo user
<<<<<<< HEAD
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getByUserId(@PathVariable Long userId) {
=======
    @GetMapping("/user")
    public ResponseEntity<List<Notification>> getByUserId(@RequestParam Long userId) {
>>>>>>> origin/main
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    // ✅ Lấy các thông báo CHƯA ĐỌC theo user
<<<<<<< HEAD
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadByUserId(@PathVariable Long userId) {
=======
    @GetMapping("/user/unread")
    public ResponseEntity<List<Notification>> getUnreadByUserId(@RequestParam Long userId) {
>>>>>>> origin/main
        return ResponseEntity.ok(service.getUnreadByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody Notification notification) {
        return ResponseEntity.ok(service.create(notification));
    }

<<<<<<< HEAD
    @PutMapping("/{id}")
    public ResponseEntity<Notification> update(@PathVariable Long id, @RequestBody Notification notification) {
=======
    @PutMapping("/update")
    public ResponseEntity<Notification> update(@RequestParam  Long id, @RequestBody Notification notification) {
>>>>>>> origin/main
        Notification updated = service.update(id, notification);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

<<<<<<< HEAD
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
=======
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long id) {
>>>>>>> origin/main
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
