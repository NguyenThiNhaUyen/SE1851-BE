package com.quyet.superapp.controller;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.enums.SeparationMethod;
import com.quyet.superapp.mapper.SeparationOrderMapper;
import com.quyet.superapp.service.SeparationOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/separation-orders")
public class SeparationOrderController {

    private final SeparationOrderService separationOrderService;

    // ✅ Tạo lệnh tách máu theo preset (có giới tính, cân nặng, phương pháp, giảm bạch cầu,...)
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/create-with-suggestion")
    public ResponseEntity<SeparationResultDTO> createWithSuggestion(
            @Valid @RequestBody CreateSeparationWithSuggestionRequest request
    ) {
        return ResponseEntity.ok(separationOrderService.createWithSuggestion(request));
    }

    // ✅ Tạo lệnh tách máu thủ công (chỉ lưu lệnh, không sinh đơn vị máu)
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/basic-manual")
    public ResponseEntity<SeparationOrderDTO> createBasicManual(@Valid @RequestBody SeparationOrderDTO dto) {
        var order = separationOrderService.createSeparationOrder(
                dto.getBloodBagId(),
                dto.getPerformedById(),
                dto.getApheresisMachineId(),
                dto.getSeparationType(),
                dto.getNote()
        );
        return ResponseEntity.ok(SeparationOrderMapper.toDTO(order));
    }

    // ✅ Tạo lệnh tách máu thủ công có sinh đơn vị máu (tự động chia theo tỉ lệ)
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/create-manual")
    public ResponseEntity<SeparationResultDTO> createManualWithUnits(
            @RequestParam Long bloodBagId,
            @RequestParam Long operatorId,
            @RequestParam(required = false) Long machineId,
            @RequestParam SeparationMethod type,
            @RequestParam(required = false) String note
    ) {
        return ResponseEntity.ok(
                separationOrderService.createSeparationOrderEntity(
                        bloodBagId, operatorId, machineId, type, note
                )
        );
    }

    // ✅ Cập nhật lệnh tách (ghi chú hoặc người thao tác)
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<SeparationOrderDTO> updateNoteOrOperator(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSeparationOrderRequest request
    ) {
        return ResponseEntity.ok(separationOrderService.updateOrder(id, request));
    }

    // ✅ Huỷ lệnh tách (chỉ ADMIN được thực hiện)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteOrder(@PathVariable Long id) {
        separationOrderService.softDeleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Lấy toàn bộ lệnh tách máu
    @GetMapping
    public ResponseEntity<List<SeparationOrderDTO>> getAll() {
        List<SeparationOrderDTO> result = separationOrderService.getAll()
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy lệnh theo loại tách
    @GetMapping("/type")
    public ResponseEntity<List<SeparationOrderDTO>> getByType(@RequestParam SeparationMethod type) {
        List<SeparationOrderDTO> result = separationOrderService.findByType(type)
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy lệnh theo nhân viên thao tác
    @GetMapping("/operator")
    public ResponseEntity<List<SeparationOrderDTO>> getByOperator(@RequestParam Long userId) {
        List<SeparationOrderDTO> result = separationOrderService.findByOperator(userId)
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy lệnh theo mã túi máu
    @GetMapping("/bag")
    public ResponseEntity<List<SeparationOrderDTO>> getByBagCode(@RequestParam String bagCode) {
        List<SeparationOrderDTO> result = separationOrderService.findByBagCode(bagCode)
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Kiểm tra túi máu đã được tách hay chưa
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkIfSeparated(@RequestParam Long bloodBagId) {
        return ResponseEntity.ok(separationOrderService.hasBeenSeparated(bloodBagId));
    }

    // ✅ Lọc theo khoảng thời gian
    @GetMapping("/time-range")
    public ResponseEntity<List<SeparationOrderDTO>> getByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<SeparationOrderDTO> result = separationOrderService.findBetween(start, end)
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Tìm kiếm nâng cao theo nhiều tiêu chí (gộp lại)
    @PostMapping("/search")
    public ResponseEntity<List<SeparationOrderDTO>> searchAdvanced(@Valid @RequestBody SeparationOrderSearchRequest request) {
        List<SeparationOrderDTO> result = separationOrderService.advancedSearch(request);
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy chi tiết 1 lệnh tách kèm danh sách đơn vị máu đã sinh
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping("/with-units")
    public ResponseEntity<SeparationOrderFullDTO> getFullOrderWithUnits(@RequestParam Long id) {
        return ResponseEntity.ok(separationOrderService.getFullOrderWithUnits(id));
    }

    // ✅ Tạo túi máu thủ công từ đơn hiến máu chưa có túi
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/create-blood-bag")
    public ResponseEntity<BloodBagDTO> createBloodBagManually(
            @RequestParam Long donationId,
            @Valid @RequestBody BloodBagDTO dto
    ) {
        BloodBag bag = separationOrderService.createManualBloodBagFromDTO(dto, donationId);
        return ResponseEntity.ok(com.quyet.superapp.mapper.BloodBagMapper.toDTO(bag));
    }

}
