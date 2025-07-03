package com.quyet.superapp.controller;

import com.quyet.superapp.dto.FullHealthCheckDTO;
import com.quyet.superapp.dto.HealthCheckFormDTO;
import com.quyet.superapp.service.HealthCheckFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health-check")
@RequiredArgsConstructor
@Validated
public class HealthCheckFormController {

    private final HealthCheckFormService healthCheckFormService;

    // ✅ Gửi phiếu khám sức khỏe (tự động đánh giá pass/fail)
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PostMapping("/submit")
    public ResponseEntity<HealthCheckFormDTO> submit(@Valid @RequestBody HealthCheckFormDTO dto) {
        HealthCheckFormDTO result = healthCheckFormService.submit(dto);
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy phiếu theo registrationId
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @GetMapping("/by-id")
    public ResponseEntity<HealthCheckFormDTO> getByRegistrationId(@RequestParam("register_id") Long regId) {
        return ResponseEntity.ok(healthCheckFormService.getByRegistrationId(regId));
    }

    // ❌ Nếu cho phép cập nhật lại (có thể bỏ qua nếu không cần)
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<HealthCheckFormDTO> update(@RequestBody HealthCheckFormDTO dto) {
        return ResponseEntity.ok(healthCheckFormService.update(dto));
    }

    // ✅ Trả cả phiếu khám + xét nghiệm máu (gộp)
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @GetMapping("/full")
    public ResponseEntity<FullHealthCheckDTO> getFullByRegisterId(@RequestParam("register_id") Long regId) {
        return ResponseEntity.ok(healthCheckFormService.getFullByRegistrationId(regId));
    }

}
