package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodSeparationRequestDTO;
import com.quyet.superapp.dto.BloodSeparationSuggestionDTO;
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.SeparationPresetConfig;
import com.quyet.superapp.repository.DonationRepository;
import com.quyet.superapp.service.BloodSeparationService;
import com.quyet.superapp.service.SeparationPresetService;
import com.quyet.superapp.util.BloodSeparationCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/separation")
@RequiredArgsConstructor
public class BloodSeparationController {

    private final BloodSeparationService separationService;
    private final SeparationPresetService presetService;
    private final BloodSeparationCalculator calculator;
    private final DonationRepository donationRepo;

    // 1️⃣ API xử lý lưu phân tách máu
    @PostMapping
    public String separateBlood(@RequestBody BloodSeparationRequestDTO dto) {
        separationService.separateBlood(dto);
        return "Đã phân tách máu thành công.";
    }

    // 2️⃣ API tính toán gợi ý phân tách từ thông tin hiến máu
    @GetMapping("/suggest")
    public BloodSeparationSuggestionDTO suggest(
            @RequestParam Long donationId,
            @RequestParam String method,
            @RequestParam boolean leukoreduced
    ) {
        Donation donation = donationRepo.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hiến máu"));

        String gender = donation.getUser().getUserProfile().getGender();
        int weight = donation.getUser().getUserProfile().;
        String bloodGroup = donation.getBloodType().getDescription();

        SeparationPresetConfig preset = presetService.getPreset(gender, weight, method, leukoreduced);

        return calculator.calculate(
                donation.getVolumeMl(),
                preset.getRbcRatio(),
                preset.getPlasmaRatio(),
                preset.getPlateletsFixed(),
                method,
                gender,
                weight,
                bloodGroup
        );
    }


}
