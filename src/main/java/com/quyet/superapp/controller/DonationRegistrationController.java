package com.quyet.superapp.controller;

import com.quyet.superapp.config.jwt.UserPrincipal;
import com.quyet.superapp.constant.MessageConstants;
import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.dto.HealthCheckFailureLogDTO;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.enums.HealthCheckFailureReason;

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

    // ✅ Đăng ký hiến máu (chỉ đặt lịch, chưa xác nhận)
    @PostMapping("/register/{userId}")
    public ResponseEntity<DonationRegistrationDTO> registerDonation(
            @PathVariable Long userId,
            @RequestBody DonationRegistrationDTO dto) {
        return ResponseEntity.ok(donationRegistrationService.register(userId, dto));
    }

    // ✅ Lấy tất cả đơn đăng ký hiến máu
    @GetMapping
    public ResponseEntity<List<DonationRegistrationDTO>> getAllRegistrations() {
        return ResponseEntity.ok(donationRegistrationService.getAllDTO());
    }

    // ✅ Lấy đơn đăng ký theo ID
    @GetMapping("/{id}")
    public ResponseEntity<DonationRegistrationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(donationRegistrationService.getDTOById(id));
    }

    // ✅ Xác nhận đơn đăng ký (chuyển trạng thái -> CONFIRMED)
    @PutMapping("/confirm")
    public ResponseEntity<DonationRegistrationDTO> confirmRegistration(
            @RequestParam("register_id") Long id) {
        return ResponseEntity.ok(donationRegistrationService.confirm(id));
    }

    //thêm unhappycase
    @GetMapping("/pending")
    public List<DonationRegistrationDTO> getPendingRegistrations() {
        return donationRegistrationService.getByStatus(DonationStatus.PENDING);
    }

    //hủy đơn nếu không đến
    @PutMapping("/cancel")
    public ResponseEntity<DonationRegistrationDTO> cancelRegistration(@RequestParam("register_id") Long id) {
        return ResponseEntity.ok(donationRegistrationService.markAsCancelled(id));
    }


    // ❌ Đánh dấu đơn không đủ điều kiện sức khỏe và ghi log
    @PutMapping("/fail-health")
    public ResponseEntity<DonationRegistrationDTO> failDueToHealth(
            @RequestParam("register_id") Long id,
            @RequestParam("reason") String reason,
            @RequestParam(value = "staff_note", required = false) String staffNote) {
        return ResponseEntity.ok(donationRegistrationService.markAsFailedHealth(id, reason, staffNote));
    }

    // ✅ Lấy danh sách log kiểm tra sức khỏe không đạt theo ID đăng ký
    @GetMapping("/health-log")
    public ResponseEntity<List<HealthCheckFailureLogDTO>> getHealthLogByRegistration(@RequestParam("register_id") Long registrationId) {
        return ResponseEntity.ok(healthCheckFailureLogService.getLogsByRegistrationId(registrationId));
    }

    @PostMapping("/confirm-donation")
    public ResponseEntity<String> confirmDonation(@RequestParam("register_id") Long regId) {
        donationRegistrationService.createDonationIfEligible(regId);
        return ResponseEntity.ok("Đã tạo bản ghi hiến máu thành công.");
    }


    /**
     * ✅ Member gửi đơn đăng ký hiến máu
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
