package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodSeparationDetailDTO;
import com.quyet.superapp.service.BloodSeparationDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/separation-details")
@RequiredArgsConstructor
public class BloodSeparationDetailController {

    private final BloodSeparationDetailService service;

    @GetMapping
    public ResponseEntity<List<BloodSeparationDetailDTO>> getAll() {
        return ResponseEntity.ok(service.getAllDetails());
    }

    @GetMapping("/log/{logId}")
    public ResponseEntity<List<BloodSeparationDetailDTO>> getByLog(@PathVariable Long logId) {
        return ResponseEntity.ok(service.getBySeparationLogId(logId));
    }

    @PostMapping
    public ResponseEntity<BloodSeparationDetailDTO> create(@RequestBody BloodSeparationDetailDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
