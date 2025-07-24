package com.quyet.superapp.controller;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.enums.SeparationMethod;
import com.quyet.superapp.mapper.BloodBagMapper;
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


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/separation-orders")
public class SeparationOrderController {

    private final SeparationOrderService separationOrderService;

    /**
     * 📌 Tạo lệnh tách máu với gợi ý (preset) – xét theo giới tính, cân nặng, phương pháp, giảm bạch cầu, v.v.
     */
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/create-with-suggestion")
    public ResponseEntity<ApiResponseDTO<SeparationResultDTO>> createWithSuggestion(@Valid @RequestBody CreateSeparationWithSuggestionRequest request) {
        var result = separationOrderService.createWithSuggestion(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Tạo lệnh tách với gợi ý thành công", result));
    }

    /**
     * 🛠 Tạo lệnh tách máu thủ công cơ bản – không sinh đơn vị máu, chỉ lưu thông tin lệnh.
     */
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/basic-manual")
    public ResponseEntity<ApiResponseDTO<SeparationOrderDTO>> createBasicManual(@Valid @RequestBody SeparationOrderDTO dto) {
        var order = separationOrderService.createSeparationOrder(
                dto.getBloodBagId(), dto.getPerformedById(), dto.getApheresisMachineId(), dto.getSeparationType(), dto.getNote()
        );
        return ResponseEntity.ok(ApiResponseDTO.success("Tạo lệnh tách thủ công thành công", SeparationOrderMapper.toDTO(order)));
    }

    /**
     * 🧪 Tạo lệnh tách thủ công kèm việc tách máu thực tế (sinh đơn vị máu theo tỷ lệ thủ công).
     */
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/create-manual")
    public ResponseEntity<ApiResponseDTO<SeparationResultDTO>> createManualWithUnits(
            @RequestParam Long bloodBagId,
            @RequestParam Long operatorId,
            @RequestParam(required = false) Long machineId,
            @RequestParam SeparationMethod type,
            @RequestParam(required = false) String note
    ) {
        var result = separationOrderService.createSeparationOrderEntity(bloodBagId, operatorId, machineId, type, note);
        return ResponseEntity.ok(ApiResponseDTO.success("Tạo lệnh và sinh đơn vị máu thành công", result));
    }

    /**
     * ✏️ Cập nhật lệnh tách máu – ghi chú và người thao tác.
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<SeparationOrderDTO>> updateNoteOrOperator(@PathVariable Long id, @Valid @RequestBody UpdateSeparationOrderRequest request) {
        var updated = separationOrderService.updateOrder(id, request);
        return ResponseEntity.ok(ApiResponseDTO.success("Cập nhật lệnh tách thành công", updated));
    }

    /**
     * ❌ Huỷ (soft delete) lệnh tách – chỉ Admin được phép.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> softDeleteOrder(@PathVariable Long id) {
        separationOrderService.softDeleteOrder(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Huỷ lệnh tách máu thành công", null));
    }

    /**
     * 📥 Lấy tất cả lệnh tách máu (không phân loại).
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<SeparationOrderDTO>>> getAll() {
        var result = separationOrderService.getAll().stream().map(SeparationOrderMapper::toDTO).toList();
        return ResponseEntity.ok(ApiResponseDTO.success("Lấy danh sách lệnh tách máu", result));
    }
    /**
     * 🔍 Lọc lệnh theo loại tách máu (plasma, tiểu cầu, RBC,...)
     */
    @GetMapping("/type")
    public ResponseEntity<ApiResponseDTO<List<SeparationOrderDTO>>> getByType(@RequestParam SeparationMethod type) {
        var result = separationOrderService.findByType(type).stream().map(SeparationOrderMapper::toDTO).toList();
        return ResponseEntity.ok(ApiResponseDTO.success("Lọc theo loại tách thành công", result));
    }

    /**
     * 👨‍🔬 Lọc theo nhân viên thao tác (operator).
     */
    @GetMapping("/operator")
    public ResponseEntity<ApiResponseDTO<List<SeparationOrderDTO>>> getByOperator(@RequestParam Long userId) {
        var result = separationOrderService.findByOperator(userId).stream().map(SeparationOrderMapper::toDTO).toList();
        return ResponseEntity.ok(ApiResponseDTO.success("Lọc theo người thao tác thành công", result));
    }

    /**
     * 🔖 Lọc theo mã túi máu (bagCode).
     */
    @GetMapping("/bag")
    public ResponseEntity<ApiResponseDTO<List<SeparationOrderDTO>>> getByBagCode(@RequestParam String bagCode) {
        var result = separationOrderService.findByBagCode(bagCode).stream().map(SeparationOrderMapper::toDTO).toList();
        return ResponseEntity.ok(ApiResponseDTO.success("Lọc theo mã túi máu thành công", result));
    }

    /**
     * ✅ Kiểm tra túi máu đã được tách chưa.
     */
    @GetMapping("/exists")
    public ResponseEntity<ApiResponseDTO<Boolean>> checkIfSeparated(@RequestParam Long bloodBagId) {
        boolean separated = separationOrderService.hasBeenSeparated(bloodBagId);
        return ResponseEntity.ok(ApiResponseDTO.success("Kiểm tra túi máu đã tách", separated));
    }

    /**
     * 🕒 Lọc lệnh tách theo khoảng thời gian (from - to).
     */
    @GetMapping("/time-range")
    public ResponseEntity<ApiResponseDTO<List<SeparationOrderDTO>>> getByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        var result = separationOrderService.findBetween(start, end).stream().map(SeparationOrderMapper::toDTO).toList();
        return ResponseEntity.ok(ApiResponseDTO.success("Lọc theo thời gian thành công", result));
    }

    /**
     * 🧠 Tìm kiếm nâng cao – theo nhiều tiêu chí (máy, nhân viên, loại tách, thời gian, v.v.).
     */
    @PostMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<SeparationOrderDTO>>> searchAdvanced(@Valid @RequestBody SeparationOrderSearchRequest request) {
        var result = separationOrderService.advancedSearch(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Tìm kiếm nâng cao thành công", result));
    }

    /**
     * 📦 Lấy chi tiết lệnh tách + danh sách đơn vị máu được sinh ra.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping("/with-units")
    public ResponseEntity<ApiResponseDTO<SeparationOrderFullDTO>> getFullOrderWithUnits(@RequestParam Long id) {
        var result = separationOrderService.getFullOrderWithUnits(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Lấy chi tiết lệnh tách máu", result));
    }

    /**
     * 🩸 Tạo túi máu thủ công từ đơn hiến máu (dành cho đơn chưa có túi máu).
     */
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/create-blood-bag")
    public ResponseEntity<ApiResponseDTO<BloodBagDTO>> createBloodBagManually(
            @RequestParam Long donationId,
            @Valid @RequestBody BloodBagDTO dto
    ) {
        BloodBag bag = separationOrderService.createManualBloodBagFromDTO(dto, donationId);
        return ResponseEntity.ok(ApiResponseDTO.success("Tạo túi máu thủ công thành công", BloodBagMapper.toDTO(bag)));
    }
}

