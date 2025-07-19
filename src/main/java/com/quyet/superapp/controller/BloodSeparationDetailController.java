package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodSeparationDetailFullDTO;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.service.BloodSeparationDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller cung cấp API để truy vấn chi tiết tách máu (BloodSeparationDetail).
 */
@RestController
@RequestMapping("/api/separation-details")
@RequiredArgsConstructor
public class BloodSeparationDetailController {

    private final BloodSeparationDetailService detailService;

    /**
     * API: Lấy danh sách chi tiết theo ID kết quả tách máu.
     * @param resultId ID của kết quả tách máu
     * @return Danh sách chi tiết tách máu đầy đủ
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/by-result")
    public ResponseEntity<List<BloodSeparationDetailFullDTO>> getByResultId(
            @RequestParam Long resultId
    ) {
        return ResponseEntity.ok(detailService.getByResultId(resultId));
    }

    /**
     * API: Lấy danh sách chi tiết theo mã túi máu.
     * @param bagCode Mã túi máu
     * @return Danh sách chi tiết tách máu đầy đủ
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/by-bag")
    public ResponseEntity<List<BloodSeparationDetailFullDTO>> getByBagCode(
            @RequestParam String bagCode
    ) {
        return ResponseEntity.ok(detailService.getByBagCode(bagCode));
    }

    /**
     * API: Lọc chi tiết tách máu theo loại thành phần (tiểu cầu, huyết tương...).
     * @param type Loại thành phần máu
     * @return Danh sách chi tiết tách máu đầy đủ
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/by-component")
    public ResponseEntity<List<BloodSeparationDetailFullDTO>> getByComponentType(
            @RequestParam BloodComponentType type
    ) {
        return ResponseEntity.ok(detailService.getByComponentType(type));
    }
}
