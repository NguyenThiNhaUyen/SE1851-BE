package com.quyet.superapp.controller;

<<<<<<< HEAD
import com.quyet.superapp.dto.DonationRequestDTO;
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.mapper.DonationMapper;
=======
import com.quyet.superapp.config.jwt.UserPrincipal;
import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.DonationHistoryDTO;
import com.quyet.superapp.dto.DonationRequestDTO;
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.mapper.DonationRequestMapper;
>>>>>>> origin/main
import com.quyet.superapp.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
=======
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
>>>>>>> origin/main
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
<<<<<<< HEAD
=======
@Validated
>>>>>>> origin/main
public class DonationController {

    private final DonationService donationService;

    // ✅ Lấy tất cả
    @GetMapping
    public ResponseEntity<List<DonationRequestDTO>> getAll() {
        List<DonationRequestDTO> dtos = donationService.getAll().stream()
<<<<<<< HEAD
                .map(DonationMapper::toDTO)
=======
                .map(DonationRequestMapper::toDTO)
>>>>>>> origin/main
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ✅ Lấy theo ID
<<<<<<< HEAD
    @GetMapping("/{id}")
    public ResponseEntity<DonationRequestDTO> getById(@PathVariable Long id) {
        return donationService.getById(id)
                .map(DonationMapper::toDTO)
=======
    @GetMapping("/by-id")
    public ResponseEntity<DonationRequestDTO> getById(@RequestParam Long id) {
        return donationService.getById(id)
                .map(DonationRequestMapper::toDTO)
>>>>>>> origin/main
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Tạo mới
    @PostMapping("/create")
<<<<<<< HEAD
    public ResponseEntity<DonationRequestDTO> create(@RequestBody Donation obj) {
        Donation saved = donationService.save(obj);
        return ResponseEntity.ok(DonationMapper.toDTO(saved));
    }

    // ✅ Cập nhật
    @PutMapping("/{id}")
    public ResponseEntity<DonationRequestDTO> update(@PathVariable Long id, @RequestBody Donation obj) {
        Optional<Donation> existing = donationService.getById(id);
        return existing.isPresent()
                ? ResponseEntity.ok(DonationMapper.toDTO(donationService.save(obj)))
=======
    public ResponseEntity<DonationRequestDTO> create( @RequestBody Donation obj) {
        Donation saved = donationService.save(obj);
        return ResponseEntity.ok(DonationRequestMapper.toDTO(saved));
    }

    // ✅ Cập nhật
    @PutMapping("/update")
    public ResponseEntity<DonationRequestDTO> update(@RequestParam Long id, @RequestBody Donation obj) {
        Optional<Donation> existing = donationService.getById(id);
        return existing.isPresent()
                ? ResponseEntity.ok(DonationRequestMapper.toDTO(donationService.save(obj)))
>>>>>>> origin/main
                : ResponseEntity.notFound().build();
    }

    // ✅ Xóa
<<<<<<< HEAD
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
=======
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
>>>>>>> origin/main
        donationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Lấy danh sách hiến máu theo userId
<<<<<<< HEAD
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DonationRequestDTO>> getByUser(@PathVariable Long userId) {
        List<DonationRequestDTO> result = donationService.getByUserId(userId).stream()
                .map(DonationMapper::toDTO)
=======
    @GetMapping("/by-user")
    public ResponseEntity<List<DonationRequestDTO>> getByUser(@RequestParam Long userId) {
        List<DonationRequestDTO> result = donationService.getByUserId(userId).stream()
                .map(DonationRequestMapper::toDTO)
>>>>>>> origin/main
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Đếm số lượt hiến theo ngày (dùng cho thống kê)
    @GetMapping("/count")
    public ResponseEntity<Long> countByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(donationService.countByDate(date));
    }

    // ✅ Lấy các đơn chưa được phân tách
    @GetMapping("/unseparated")
    public ResponseEntity<List<DonationRequestDTO>> getUnseparated() {
        List<DonationRequestDTO> result = donationService.getUnseparatedDonations().stream()
<<<<<<< HEAD
                .map(DonationMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
=======
                .map(DonationRequestMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    //xem lịch sử hiến máu
    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<DonationHistoryDTO>>> getUserHistory(@RequestParam("userId") Long userId) {
        List<DonationHistoryDTO> history = donationService.getHistoryByUserId(userId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Lịch sử hiến máu", history));
    }

    @GetMapping("/history/self")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ApiResponseDTO<List<DonationHistoryDTO>>> getSelfHistory(@AuthenticationPrincipal UserPrincipal principal) {
        List<DonationHistoryDTO> history = donationService.getHistoryByUserId(principal.getUserId());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Lịch sử hiến máu của bạn", history));
    }
>>>>>>> origin/main
}
