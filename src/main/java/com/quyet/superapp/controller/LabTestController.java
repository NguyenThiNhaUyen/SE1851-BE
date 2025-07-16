package com.quyet.superapp.controller;

import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.CreateLabTestRequest;
import com.quyet.superapp.dto.LabTestResultDTO;
import com.quyet.superapp.service.LabTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab-tests")
@RequiredArgsConstructor
public class LabTestController {

    private final LabTestService labTestService;

    /**
     * 🧪 Tạo kết quả xét nghiệm cho một đơn vị máu.
     * – Kiểm tra đơn vị máu có tồn tại không
     * – Kiểm tra đã xét nghiệm chưa
     * – Ghi nhận kết quả âm tính/đạt chuẩn
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<LabTestResultDTO>> createLabTest(@Valid @RequestBody CreateLabTestRequest request) {
        LabTestResultDTO result = labTestService.createLabTestResult(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Tạo kết quả xét nghiệm thành công", result));
    }

    /**
     * 🔍 Lấy kết quả xét nghiệm theo đơn vị máu.
     */
    @GetMapping("/by-blood-unit")
    public ResponseEntity<ApiResponseDTO<LabTestResultDTO>> getByBloodUnit(@RequestParam Long bloodUnitId) {
        return labTestService.getByBloodUnit(bloodUnitId)
                .map(result -> ResponseEntity.ok(ApiResponseDTO.success("Lấy kết quả thành công", result)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDTO.fail("Không tìm thấy kết quả xét nghiệm")));
    }

    /**
     * ✅ Kiểm tra đơn vị máu đã được xét nghiệm hay chưa.
     * – Trả về true/false
     */
    @GetMapping("/check")
    public ResponseEntity<ApiResponseDTO<Boolean>> checkTested(@RequestParam Long bloodUnitId) {
        boolean result = labTestService.isTested(bloodUnitId);
        return ResponseEntity.ok(ApiResponseDTO.success("Kiểm tra xét nghiệm thành công", result));
    }

    /**
     * 📋 Lấy tất cả kết quả xét nghiệm trong hệ thống.
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LabTestResultDTO>>> getAll() {
        List<LabTestResultDTO> results = labTestService.getAllResults();
        return ResponseEntity.ok(ApiResponseDTO.success("Lấy danh sách kết quả xét nghiệm", results));
    }

    /**
     * ❌ Xoá kết quả xét nghiệm theo ID.
     * – Cẩn thận: hành động không thể hoàn tác.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponseDTO<Void>> deleteResult(@RequestParam Long labTestResultId) {
        labTestService.deleteResult(labTestResultId);
        return ResponseEntity.ok(ApiResponseDTO.success("Xoá kết quả xét nghiệm thành công", null));
    }
}
