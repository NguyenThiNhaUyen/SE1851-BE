package com.quyet.superapp.controller;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.service.SeparationStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/separation-statistics")
public class SeparationStatisticsController {

    private final SeparationStatisticsService statsService;

    @GetMapping("/volume/day")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<SeparationVolumeStatsDTO>> getVolumeByDay(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(statsService.getVolumeStatsByDay(start, end));
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/volume/month")
    public ResponseEntity<List<SeparationVolumeStatsDTO>> getVolumeByMonth() {
        return ResponseEntity.ok(statsService.getVolumeStatsByMonth());
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/low-volume")
    public ResponseEntity<List<LowVolumeRatioDTO>> getLowVolumeRatio(
            @RequestParam(defaultValue = "10") int threshold
    ) {
        return ResponseEntity.ok(statsService.getLowVolumeRatios(threshold));
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/quality-average")
    public ResponseEntity<List<QualityRatingAverageDTO>> getAvgRatings() {
        return ResponseEntity.ok(statsService.getAverageQualityRatings());
    }

    @GetMapping("/rejection-rate")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<RejectedUnitStatsDTO>> getRejectedStats(
            @RequestParam(defaultValue = "10") int volumeThreshold) {
        return ResponseEntity.ok(statsService.getRejectedUnitsStats(volumeThreshold));
    }

    //✅ Thống kê theo nhóm máu
    @GetMapping("/blood-type-summary")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<BloodTypeSeparationStatsDTO>> getByBloodType() {
        return ResponseEntity.ok(statsService.getStatsByBloodType());
    }
    // ✅ Tổng số lần tách máu theo ngày
    @GetMapping("/count-per-day")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<SeparationCountDTO>> getCountPerDay() {
        return ResponseEntity.ok(statsService.getSeparationCountPerDay());
    }

}
