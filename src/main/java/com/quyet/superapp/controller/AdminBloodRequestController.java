package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodRequestDTO;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.mapper.BloodRequestMapper;
import com.quyet.superapp.service.BloodRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/blood-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminBloodRequestController {

    private final BloodRequestService service;

    // ✅ Lấy danh sách tất cả yêu cầu máu
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getAllRequests() {
        return ResponseEntity.ok(service.getAllRequests());
    }

    // ✅ Duyệt yêu cầu
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodRequestDTO> approveRequest(@PathVariable Long id) {
        BloodRequest updated = service.updateStatus(id, "APPROVED");
        return ResponseEntity.ok(BloodRequestMapper.toDTO(updated));
    }

    // ✅ Từ chối yêu cầu
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodRequestDTO> rejectRequest(@PathVariable Long id) {
        BloodRequest updated = service.updateStatus(id, "REJECTED");
        return ResponseEntity.ok(BloodRequestMapper.toDTO(updated));
    }
}
