package com.quyet.superapp.controller;

import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.FullDonationProcessRequest;
import com.quyet.superapp.service.DonationProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/donation-process")
@RequiredArgsConstructor
public class DonationProcessController {

    private final DonationProcessService donationProcessService;

    /**
     * ✅ Xử lý toàn bộ quy trình: khám sức khỏe + xét nghiệm --> hiến máu
     */
    @PostMapping("/execute")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponseDTO<String>> executeFullProcess(
            @RequestBody FullDonationProcessRequest request
    ) {
        donationProcessService.handleFullDonationProcess(request);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Xử lý quy trình hiến máu thành công", null));
    }
}
