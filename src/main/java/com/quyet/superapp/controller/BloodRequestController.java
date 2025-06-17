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
@RequestMapping("/api/blood-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BloodRequestController {

    private final BloodRequestService requestService;

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<BloodRequestDTO> create(@RequestBody BloodRequestDTO dto) {
        BloodRequest created = requestService.createRequest(dto);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(created));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getAllRequestsForAdmin() {
        List<BloodRequestDTO> list = requestService.getAllRequests();
        return ResponseEntity.ok(list);
    }

}
