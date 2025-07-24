package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodComponentDTO;
import com.quyet.superapp.service.BloodComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-components")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BloodComponentController {

    private final BloodComponentService service;

    @GetMapping
    public ResponseEntity<List<BloodComponentDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodComponentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodComponentDTO> create(@RequestBody BloodComponentDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodComponentDTO> update(@PathVariable Long id, @RequestBody BloodComponentDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-code")
    public ResponseEntity<BloodComponentDTO> getByCode(@RequestParam String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }
}
