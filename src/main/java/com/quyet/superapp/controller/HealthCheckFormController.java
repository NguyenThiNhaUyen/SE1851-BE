package com.quyet.superapp.controller;

import com.quyet.superapp.dto.HealthCheckFormDTO;
import com.quyet.superapp.service.HealthCheckFormService;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
=======
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
>>>>>>> origin/main
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health-check")
@RequiredArgsConstructor
<<<<<<< HEAD
=======
@Validated
>>>>>>> origin/main
public class HealthCheckFormController {

    private final HealthCheckFormService healthCheckFormService;

    // ✅ Gửi phiếu khám sức khỏe (tự động đánh giá pass/fail)
<<<<<<< HEAD
    @PostMapping("/submit")
    public ResponseEntity<HealthCheckFormDTO> submit(@RequestBody HealthCheckFormDTO dto) {
=======
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PostMapping("/submit")
    public ResponseEntity<HealthCheckFormDTO> submit(@Valid @RequestBody HealthCheckFormDTO dto) {
>>>>>>> origin/main
        HealthCheckFormDTO result = healthCheckFormService.submit(dto);
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy phiếu theo registrationId
<<<<<<< HEAD
    @GetMapping
=======
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @GetMapping("/by-id")
>>>>>>> origin/main
    public ResponseEntity<HealthCheckFormDTO> getByRegistrationId(@RequestParam("register_id") Long regId) {
        return ResponseEntity.ok(healthCheckFormService.getByRegistrationId(regId));
    }

    // ❌ Nếu cho phép cập nhật lại (có thể bỏ qua nếu không cần)
<<<<<<< HEAD
=======
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
>>>>>>> origin/main
    @PutMapping("/update")
    public ResponseEntity<HealthCheckFormDTO> update(@RequestBody HealthCheckFormDTO dto) {
        return ResponseEntity.ok(healthCheckFormService.update(dto));
    }

<<<<<<< HEAD
=======

>>>>>>> origin/main
}
