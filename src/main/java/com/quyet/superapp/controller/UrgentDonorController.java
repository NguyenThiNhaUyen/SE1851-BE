package com.quyet.superapp.controller;

import com.quyet.superapp.dto.UrgentDonorRegistrationDTO;
import com.quyet.superapp.dto.UrgentDonorResponseDTO;
import com.quyet.superapp.entity.UrgentDonorRegistry;
import com.quyet.superapp.service.UrgentDonorRegistryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/urgent-donors")
@RequiredArgsConstructor
@Validated
public class UrgentDonorController {

    private final UrgentDonorRegistryService urgentDonorRegistryService;

    /**
     * Đăng ký người hiến máu khẩn cấp.
     */
    @PostMapping
    public ResponseEntity<?> registerUrgentDonor(@RequestBody UrgentDonorRegistrationDTO dto) {
        urgentDonorRegistryService.registerUrgentDonor(dto);
        return ResponseEntity.ok("✅ Đã thêm người hiến máu khẩn cấp");
    }

    /**
     * Lấy tất cả người hiến máu đang sẵn sàng.
     */
    @GetMapping
    public ResponseEntity<List<UrgentDonorRegistry>> getAllAvailableDonors() {
        return ResponseEntity.ok(urgentDonorRegistryService.getAllAvailableDonors());
    }

    /**
     * Tìm người hiến gần một vị trí.
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<UrgentDonorRegistry>> getNearbyDonors(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "10") double radiusKm) {
        return ResponseEntity.ok(
                urgentDonorRegistryService.findNearbyDonors(lat, lng, radiusKm)
        );
    }

    /**
     * Lọc người hiến máu theo nhóm máu và khoảng cách.
     */
    @GetMapping("/filter")
    public ResponseEntity<List<UrgentDonorResponseDTO>> filterDonors(
            @RequestParam Long bloodTypeId,
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "10") double radiusKm) {
        return ResponseEntity.ok(
                urgentDonorRegistryService.filterDonorsByBloodTypeAndDistance(bloodTypeId, lat, lng, radiusKm)
        );
    }
}
