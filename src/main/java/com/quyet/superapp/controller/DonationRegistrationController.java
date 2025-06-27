package com.quyet.superapp.controller;

import com.quyet.superapp.constant.MessageConstants;
import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.dto.HealthCheckFailureLogDTO;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.service.DonationRegistrationService;
import com.quyet.superapp.service.HealthCheckFailureLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/donation")
@RequiredArgsConstructor
@Validated
public class DonationRegistrationController {

    private final DonationRegistrationService donationRegistrationService;
    private final HealthCheckFailureLogService healthCheckFailureLogService;
    /**
     * Đăng ký hiến máu (chỉ đặt lịch, chưa xác nhận)
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerDonation(
            @RequestParam Long userId,
            @RequestBody DonationRegistrationDTO dto) {
        return donationRegistrationService.register(userId, dto);
    }
    /**
     * Lấy tất cả đơn đăng ký hiến máu
     */
    @GetMapping
    public ResponseEntity<?> getAllRegistrations() {
        return donationRegistrationService.getAllDTO();
    }
    /**
     * Lấy đơn đăng ký theo ID
     */
    @GetMapping("/by-id")
    public ResponseEntity<?> getById(@RequestParam Long id) {
        return donationRegistrationService.getDTOById(id);
    }
    /**
     * Xác nhận đơn đăng ký (chuyển trạng thái -> CONFIRMED)
     */
    @PutMapping("/confirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("register_id") Long id) {
        return donationRegistrationService.confirm(id); // KHÔNG cần truyền username
    }
    /**
     * Lấy danh sách đơn đang chờ xác nhận
     */
    @GetMapping("/pending")
    public List<DonationRegistrationDTO> getPendingRegistrations() {
        return donationRegistrationService.getByStatus(DonationStatus.PENDING);
    }
    /**
     * Hủy đơn nếu người đăng ký không đến
     */
    @PutMapping("/cancel")
    public ResponseEntity<?> cancelRegistration(@RequestParam("register_id") Long id) {
        return donationRegistrationService.markAsCancelled(id);
    }
    /**
     * Đánh dấu đơn không đủ điều kiện sức khỏe và ghi log
     */
    @PutMapping("/fail-health")
    public ResponseEntity<?> failDueToHealth(
            @RequestParam("register_id") Long id,
            @RequestParam("reason") String reason,
            @RequestParam(value = "staff_note", required = false) String staffNote) {
        return donationRegistrationService.markAsFailedHealth(id, reason, staffNote);
    }
    /**
     * Lấy danh sách log kiểm tra sức khỏe không đạt theo ID đăng ký
     */
    @GetMapping("/health-log")
    public ResponseEntity<ApiResponseDTO<List<HealthCheckFailureLogDTO>>> getHealthLogByRegistration(@RequestParam("register_id") Long registrationId) {
        List<HealthCheckFailureLogDTO> logs = healthCheckFailureLogService.getLogsByRegistrationId(registrationId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.GET_REGISTRATION_SUCCESS, logs));
    }
    /**
     * Tạo bản ghi hiến máu nếu người đăng ký đủ điều kiện sức khỏe
     */
    @PostMapping("/confirm-donation")
    public ResponseEntity<?> confirmDonation(@RequestParam("register_id") Long regId) {
        return donationRegistrationService.createDonationIfEligible(regId);
    }
}
