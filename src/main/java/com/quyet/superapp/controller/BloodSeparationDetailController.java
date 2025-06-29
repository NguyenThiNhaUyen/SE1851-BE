package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodSeparationDetailFullDTO;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.service.BloodSeparationDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/separation-details")
@RequiredArgsConstructor
public class BloodSeparationDetailController {

    private final BloodSeparationDetailService detailService;

    // 🔍 Lấy theo resultId
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/by-result")
    public ResponseEntity<List<BloodSeparationDetailFullDTO>> getByResultId(@RequestParam Long resultId) {
        return ResponseEntity.ok(detailService.getDetailsByResultId(resultId));
    }

    // 🔍 Lấy theo mã túi máu
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/by-bag")
    public ResponseEntity<List<BloodSeparationDetailFullDTO>> getByBagCode(@RequestParam String bagCode) {
        return ResponseEntity.ok(detailService.getDetailsByBagCode(bagCode));
    }

    // 🔍 Lọc theo loại thành phần
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/by-component")
    public ResponseEntity<List<BloodSeparationDetailFullDTO>> getByComponentType(@RequestParam BloodComponentType type) {
        return ResponseEntity.ok(detailService.getDetailsByComponentType(type));
    }
}
