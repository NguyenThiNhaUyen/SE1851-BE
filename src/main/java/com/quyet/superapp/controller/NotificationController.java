package com.quyet.superapp.controller;

import com.quyet.superapp.entity.Notification;
import com.quyet.superapp.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/by-id")
    public ResponseEntity<Notification> getById(@RequestParam Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Lấy tất cả thông báo theo user
    @GetMapping("/user")
    public ResponseEntity<List<Notification>> getByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    // ✅ Lấy các thông báo CHƯA ĐỌC theo user
    @GetMapping("/user/unread")
    public ResponseEntity<List<Notification>> getUnreadByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getUnreadByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody Notification notification) {
        return ResponseEntity.ok(service.create(notification));
    }

    @PutMapping("/update")
    public ResponseEntity<Notification> update(@RequestParam  Long id, @RequestBody Notification notification) {
        Notification updated = service.update(id, notification);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
