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

    private final BloodComponentService bloodComponentService;

    @GetMapping
    public ResponseEntity<List<BloodComponentDTO>> getAllComponents() {
        return ResponseEntity.ok(bloodComponentService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodComponentDTO> create(@RequestBody BloodComponentDTO dto) {
        BloodComponentDTO created = bloodComponentService.create(dto);
        return ResponseEntity.ok(created);
    }
}
