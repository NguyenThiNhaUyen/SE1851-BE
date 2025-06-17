package com.quyet.superapp.controller;

import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.service.DonationRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donation")
@RequiredArgsConstructor
public class DonationRegistrationController {

    private final DonationRegistrationService donationRegistrationService;

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
}
