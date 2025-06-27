package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodRequestConfirmDTO;
import com.quyet.superapp.dto.BloodRequestDTO;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.mapper.BloodRequestMapper;
import com.quyet.superapp.service.BloodRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class StaffBloodRequestController {

    private final BloodRequestService service;

    @PostMapping("/confirm")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<BloodRequestDTO> confirmReceived(@Valid @RequestBody BloodRequestConfirmDTO dto) {
        BloodRequest confirmed = service.confirmReceivedVolume(dto.getRequestId(), dto.getConfirmedVolumeMl());
        return ResponseEntity.ok(BloodRequestMapper.toDTO(confirmed));
    }
}
