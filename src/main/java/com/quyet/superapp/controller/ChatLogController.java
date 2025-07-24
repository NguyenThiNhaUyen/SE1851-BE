package com.quyet.superapp.controller;

import com.quyet.superapp.dto.ChatLogDTO;
import com.quyet.superapp.service.ChatLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatlogs")
@RequiredArgsConstructor
@Validated
public class ChatLogController {

    private final ChatLogService chatLogService;

    @GetMapping
    public List<ChatLogDTO> getAll() {
        return chatLogService.getAll();
    }

    @GetMapping("/by-id")
    public ResponseEntity<ChatLogDTO> getById(@RequestParam Long id) {
        return chatLogService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user")
    public List<ChatLogDTO> getByUserId(@RequestParam Long userId) {
        return chatLogService.getByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<ChatLogDTO> create(@Valid @RequestBody ChatLogDTO dto) {
        try {
            return ResponseEntity.ok(chatLogService.create(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        chatLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
