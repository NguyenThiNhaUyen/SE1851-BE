package com.quyet.superapp.service;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.entity.BloodSeparationDetail;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.repository.BloodSeparationDetailRepository;
import com.quyet.superapp.repository.SeparationOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeparationStatisticsService {

    private final BloodSeparationDetailRepository detailRepository;
    private final SeparationOrderRepository separationOrderRepository;

    public List<SeparationVolumeStatsDTO> getVolumeStatsByDay(LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return detailRepository.getVolumeStatsByDayRange(start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        }
        return detailRepository.getVolumeStats("%Y-%m-%d");
    }

    public List<SeparationVolumeStatsDTO> getVolumeStatsByMonth() {
        return detailRepository.getVolumeStats("%Y-%m");
    }

    public List<LowVolumeRatioDTO> getLowVolumeRatios(int threshold) {
        List<BloodSeparationDetail> all = detailRepository.findAll();
        Map<BloodComponentType, List<BloodSeparationDetail>> grouped = all.stream()
                .collect(Collectors.groupingBy(BloodSeparationDetail::getComponentType));

        return grouped.entrySet().stream().map(entry -> {
            long total = entry.getValue().size();
            long lowCount = entry.getValue().stream().filter(d -> d.getVolumeMl() < threshold).count();
            double percent = total == 0 ? 0.0 : (lowCount * 100.0 / total);
            return new LowVolumeRatioDTO(entry.getKey(), total, lowCount, percent);
        }).toList();
    }

    public List<QualityRatingAverageDTO> getAverageQualityRatings() {
        List<BloodSeparationDetail> all = detailRepository.findAll();

        Map<BloodComponentType, List<BloodSeparationDetail>> grouped = all.stream()
                .collect(Collectors.groupingBy(BloodSeparationDetail::getComponentType));

        return grouped.entrySet().stream().map(entry -> {
            double avg = entry.getValue().stream()
                    .mapToInt(d -> convertRatingToScore(d.getQualityRating()))
                    .average().orElse(0.0);
            return new QualityRatingAverageDTO(entry.getKey(), avg);
        }).toList();
    }

    public List<RejectedUnitStatsDTO> getRejectedUnitsStats(int volumeThreshold) {
        List<BloodSeparationDetail> all = detailRepository.findAll();

        Map<BloodComponentType, List<BloodSeparationDetail>> grouped = all.stream()
                .collect(Collectors.groupingBy(BloodSeparationDetail::getComponentType));

        return grouped.entrySet().stream().map(entry -> {
            long total = entry.getValue().size();
            long rejected = entry.getValue().stream().filter(d ->
                    d.getVolumeMl() < volumeThreshold ||
                            convertRatingToScore(d.getQualityRating()) <= 2
            ).count();
            double percent = total == 0 ? 0.0 : (rejected * 100.0 / total);
            return new RejectedUnitStatsDTO(entry.getKey(), total, rejected, percent);
        }).toList();
    }
    public List<SeparationCountDTO> getSeparationCountPerDay() {
        return separationOrderRepository.countSeparationByDay()
                .stream()
                .map(obj -> new SeparationCountDTO((String) obj[0], (Long) obj[1]))
                .toList();
    }

    public List<BloodTypeSeparationStatsDTO> getStatsByBloodType() {
        return separationOrderRepository.countByBloodType().stream()
                .map(obj -> new BloodTypeSeparationStatsDTO((String) obj[0], (Long) obj[1]))
                .toList();
    }

    private int convertRatingToScore(String rating) {
        return switch (rating.toUpperCase()) {
            case "A" -> 4;
            case "B" -> 3;
            case "C" -> 2;
            case "D" -> 1;
            default -> 0;
        };
    }
}
