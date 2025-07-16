package com.quyet.superapp.controller;

import com.quyet.superapp.config.jwt.UserPrincipal;
import com.quyet.superapp.constant.MessageConstants;
import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.dto.HealthCheckFailureLogDTO;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.enums.HealthCheckFailureReason;
import com.quyet.superapp.service.DonationRegistrationService;
import com.quyet.superapp.service.HealthCheckFailureLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donation")
@RequiredArgsConstructor
@Validated
public class DonationRegistrationController {

    private final DonationRegistrationService donationRegistrationService;
    private final HealthCheckFailureLogService healthCheckFailureLogService;

    /**
     * ✅ Thành viên gửi đơn đăng ký hiến máu
     */
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody DonationRegistrationDTO dto
    ) {
        return donationRegistrationService.register(principal.getUserId(), dto);
    }
    /**
     * ✅ Lấy tất cả đơn đăng ký (chỉ dành cho staff/admin)
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        return donationRegistrationService.getAllDTO();
    }
    /**
     * ✅ Lấy đơn đăng ký theo ID
     */
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/by-id")
    public ResponseEntity<?> getById(@RequestParam Long id) {
        return donationRegistrationService.getDTOById(id);
    }
    /**
     * ✅ Lấy danh sách đơn đang chờ duyệt
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/pending")
    public ResponseEntity<?> getPending() {
        List<DonationRegistrationDTO> result = donationRegistrationService.getByStatus(DonationStatus.PENDING);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.GET_REGISTRATION_SUCCESS, result));
    }
    /**
     * ✅ Xác nhận đơn đăng ký (staff)
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/confirm")
    public ResponseEntity<?> confirm(
            @RequestParam("register_id") Long regId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return donationRegistrationService.confirm(regId, principal);
    }
    /**
     * ✅ Hủy đơn do không đến hiến (staff/admin)
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/cancel")
    public ResponseEntity<?> cancel(@RequestParam("register_id") Long regId) {
        return donationRegistrationService.markAsCancelled(regId);
    }
    /**
     * ✅ Đánh dấu không đủ điều kiện sức khỏe (staff/admin)
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/fail-health")
    public ResponseEntity<?> failHealth(
            @RequestParam("register_id") Long regId,
            @RequestParam("reason") HealthCheckFailureReason reason,
            @RequestParam(value = "staff_note", required = false) String staffNote
    ) {
        return donationRegistrationService.markAsFailedHealth(regId, reason, staffNote);
    }
    /**
     * ✅ Lấy danh sách log thất bại sức khỏe của đơn đăng ký
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/health-log")
    public ResponseEntity<?> getHealthLog(@RequestParam("register_id") Long regId) {
        List<HealthCheckFailureLogDTO> logs = healthCheckFailureLogService.getLogsByRegistrationId(regId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.GET_REGISTRATION_SUCCESS, logs));
    }
    /**
     * ✅ Đánh dấu DONATED nếu đã hiến (staff/admin)
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/mark-donated")
    public ResponseEntity<?> markDonated(@RequestParam("register_id") Long regId) {
        return donationRegistrationService.markAsDonated(regId);
    }
}
