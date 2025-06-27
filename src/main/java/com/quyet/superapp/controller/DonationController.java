package com.quyet.superapp.controller;

import com.quyet.superapp.dto.DonationRequestDTO;
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.mapper.DonationMapper;
import com.quyet.superapp.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
@Validated
public class DonationController {

    private final DonationService donationService;

    // ✅ Lấy tất cả
    @GetMapping
    public ResponseEntity<List<DonationRequestDTO>> getAll() {
        List<DonationRequestDTO> dtos = donationService.getAll().stream()
                .map(DonationMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ✅ Lấy theo ID
    @GetMapping("/by-id")
    public ResponseEntity<DonationRequestDTO> getById(@RequestParam Long id) {
        return donationService.getById(id)
                .map(DonationMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Tạo mới
    @PostMapping("/create")
    public ResponseEntity<DonationRequestDTO> create( @RequestBody Donation obj) {
        Donation saved = donationService.save(obj);
        return ResponseEntity.ok(DonationMapper.toDTO(saved));
    }

    // ✅ Cập nhật
    @PutMapping("/update")
    public ResponseEntity<DonationRequestDTO> update(@RequestParam Long id, @RequestBody Donation obj) {
        Optional<Donation> existing = donationService.getById(id);
        return existing.isPresent()
                ? ResponseEntity.ok(DonationMapper.toDTO(donationService.save(obj)))
                : ResponseEntity.notFound().build();
    }

    // ✅ Xóa
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        donationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Lấy danh sách hiến máu theo userId
    @GetMapping("/by-user")
    public ResponseEntity<List<DonationRequestDTO>> getByUser(@RequestParam Long userId) {
        List<DonationRequestDTO> result = donationService.getByUserId(userId).stream()
                .map(DonationMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Đếm số lượt hiến theo ngày (dùng cho thống kê)
    @GetMapping("/count")
    public ResponseEntity<Long> countByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime date) {
        return ResponseEntity.ok(donationService.countByDate(date));
    }

    // ✅ Lấy các đơn chưa được phân tách
    @GetMapping("/unseparated")
    public ResponseEntity<List<DonationRequestDTO>> getUnseparated() {
        List<DonationRequestDTO> result = donationService.getUnseparatedDonations().stream()
                .map(DonationMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
