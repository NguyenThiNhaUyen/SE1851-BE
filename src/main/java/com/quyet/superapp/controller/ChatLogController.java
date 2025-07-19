package com.quyet.superapp.controller;

import com.quyet.superapp.dto.ChatLogDTO;
import com.quyet.superapp.service.ChatLogService;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
=======
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
>>>>>>> origin/main
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatlogs")
@RequiredArgsConstructor
<<<<<<< HEAD
=======
@Validated
>>>>>>> origin/main
public class ChatLogController {

    private final ChatLogService chatLogService;

    @GetMapping
    public List<ChatLogDTO> getAll() {
        return chatLogService.getAll();
    }

<<<<<<< HEAD
    @GetMapping("/{id}")
    public ResponseEntity<ChatLogDTO> getById(@PathVariable Long id) {
=======
    @GetMapping("/by-id")
    public ResponseEntity<ChatLogDTO> getById(@RequestParam Long id) {
>>>>>>> origin/main
        return chatLogService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

<<<<<<< HEAD
    @GetMapping("/user/{userId}")
    public List<ChatLogDTO> getByUserId(@PathVariable Long userId) {
=======
    @GetMapping("/user")
    public List<ChatLogDTO> getByUserId(@RequestParam Long userId) {
>>>>>>> origin/main
        return chatLogService.getByUserId(userId);
    }

    @PostMapping
<<<<<<< HEAD
    public ResponseEntity<ChatLogDTO> create(@RequestBody ChatLogDTO dto) {
=======
    public ResponseEntity<ChatLogDTO> create(@Valid @RequestBody ChatLogDTO dto) {
>>>>>>> origin/main
        try {
            return ResponseEntity.ok(chatLogService.create(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

<<<<<<< HEAD
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
=======
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long id) {
>>>>>>> origin/main
        chatLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
