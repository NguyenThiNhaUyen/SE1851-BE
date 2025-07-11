package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodSeparationSuggestionDTO;
import com.quyet.superapp.dto.CreateSeparationWithSuggestionRequest;
import com.quyet.superapp.dto.SeparationOrderDTO;
import com.quyet.superapp.dto.SeparationResultDTO;
import com.quyet.superapp.entity.SeparationOrder;
import com.quyet.superapp.enums.SeparationMethod;
import com.quyet.superapp.mapper.SeparationOrderMapper;
import com.quyet.superapp.service.SeparationOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/separation-orders")
public class SeparationOrderController {

    private final SeparationOrderService separationOrderService;

    // ✅ Tạo mới lệnh tách máu có tính toán gợi ý
    @PostMapping("/create-with-suggestion")
    public ResponseEntity<SeparationResultDTO> createWithSuggestion(@RequestBody CreateSeparationWithSuggestionRequest request) {
        return ResponseEntity.ok(separationOrderService.createWithSuggestion(request));
    }

    // ✅ Tạo mới lệnh tách máu thủ công
    @PostMapping
    public ResponseEntity<SeparationOrderDTO> create(@RequestBody SeparationOrderDTO dto) {
        var order = separationOrderService.createSeparationOrder(
                dto.getBloodBagId(),
                dto.getPerformedById(),
                dto.getApheresisMachineId(),
                dto.getSeparationType(),
                dto.getNote()
        );
        return ResponseEntity.ok(SeparationOrderMapper.toDTO(order));
    }

    // ✅ Lấy tất cả lệnh tách máu
    @GetMapping
    public ResponseEntity<List<SeparationOrderDTO>> getAll() {
        List<SeparationOrderDTO> result = separationOrderService.getAll()
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy theo loại tách
    @GetMapping("/type/{type}")
    public ResponseEntity<List<SeparationOrderDTO>> getByType(@PathVariable SeparationMethod type) {
        List<SeparationOrderDTO> result = separationOrderService.findByType(type)
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy theo nhân viên thao tác
    @GetMapping("/operator/{userId}")
    public ResponseEntity<List<SeparationOrderDTO>> getByOperator(@PathVariable Long userId) {
        List<SeparationOrderDTO> result = separationOrderService.findByOperator(userId)
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy theo mã túi máu
    @GetMapping("/bag/{bagCode}")
    public ResponseEntity<List<SeparationOrderDTO>> getByBagCode(@PathVariable String bagCode) {
        List<SeparationOrderDTO> result = separationOrderService.findByBagCode(bagCode)
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Kiểm tra túi máu đã được tách chưa
    @GetMapping("/exists/{bloodBagId}")
    public ResponseEntity<Boolean> checkIfSeparated(@PathVariable Long bloodBagId) {
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

}
