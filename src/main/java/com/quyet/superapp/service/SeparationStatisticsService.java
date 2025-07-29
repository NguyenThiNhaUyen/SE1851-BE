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

    /**
     * Thống kê tổng thể thể tích từng loại thành phần máu theo ngày
     */
    public List<SeparationVolumeStatsDTO> getVolumeStatsByDay(LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return detailRepository.getVolumeStatsByDayRange(start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        }
        return detailRepository.getVolumeStats("%Y-%m-%d");
    }

    /**
     * Thống kê thể tích phân tách theo tháng
     */
    public List<SeparationVolumeStatsDTO> getVolumeStatsByMonth() {
        return detailRepository.getVolumeStats("%Y-%m");
    }

    /**
     * Tỷ lệ các túi có thể tích thấp dưới ngưỡng
     */
    public List<LowVolumeRatioDTO> getLowVolumeRatios(int threshold) {
        List<BloodSeparationDetail> allDetails = detailRepository.findAll();

        Map<BloodComponentType, List<BloodSeparationDetail>> groupedByType = allDetails.stream()
                .collect(Collectors.groupingBy(BloodSeparationDetail::getComponentType));

        return groupedByType.entrySet().stream()
                .map(entry -> {
                    long total = entry.getValue().size();
                    long lowVolumeCount = entry.getValue().stream()
                            .filter(detail -> detail.getVolumeMl() < threshold)
                            .count();

                    double percentage = total == 0 ? 0.0 : (lowVolumeCount * 100.0 / total);
                    return new LowVolumeRatioDTO(entry.getKey(), total, lowVolumeCount, percentage);
                }).toList();
    }

    /**
     * Điểm chất lượng trung bình theo từng loại thành phần máu
     */
    public List<QualityRatingAverageDTO> getAverageQualityRatings() {
        List<BloodSeparationDetail> allDetails = detailRepository.findAll();

        Map<BloodComponentType, List<BloodSeparationDetail>> groupedByType = allDetails.stream()
                .collect(Collectors.groupingBy(BloodSeparationDetail::getComponentType));

        return groupedByType.entrySet().stream()
                .map(entry -> {
                    double avgScore = entry.getValue().stream()
                            .mapToInt(d -> convertRatingToScore(d.getQualityRating()))
                            .average().orElse(0.0);
                    return new QualityRatingAverageDTO(entry.getKey(), avgScore);
                }).toList();
    }

    /**
     * Số đơn phân tách theo từng ngày
     */
    public List<SeparationCountDTO> getSeparationCountPerDay() {
        return separationOrderRepository.countSeparationByDay().stream()
                .map(obj -> new SeparationCountDTO((String) obj[0], (Long) obj[1]))
                .toList();
    }

    /**
     * Thống kê số lượng và tỷ lệ túi máu bị loại bỏ (do thể tích thấp hoặc chất lượng kém)
     */
    public List<RejectedUnitStatsDTO> getRejectedUnitsStats(int volumeThreshold) {
        List<BloodSeparationDetail> allDetails = detailRepository.findAll();

        Map<BloodComponentType, List<BloodSeparationDetail>> groupedByType = allDetails.stream()
                .collect(Collectors.groupingBy(BloodSeparationDetail::getComponentType));

        return groupedByType.entrySet().stream()
                .map(entry -> {
                    long total = entry.getValue().size();
                    long rejected = entry.getValue().stream()
                            .filter(d -> d.getVolumeMl() < volumeThreshold || convertRatingToScore(d.getQualityRating()) <= 2)
                            .count();

                    double percent = total == 0 ? 0.0 : (rejected * 100.0 / total);
                    return new RejectedUnitStatsDTO(entry.getKey(), total, rejected, percent);
                }).toList();
    }

    /**
     * Thống kê số đơn phân tách theo từng nhóm máu
     */
    public List<BloodTypeSeparationStatsDTO> getStatsByBloodType() {
        return separationOrderRepository.countByBloodType().stream()
                .map(obj -> new BloodTypeSeparationStatsDTO((String) obj[0], (Long) obj[1]))
                .toList();
    }

    /**
     * Chuyển đổi xếp hạng chất lượng từ chữ sang điểm số
     */
    private int convertRatingToScore(String rating) {
        return switch (rating != null ? rating.toUpperCase() : "") {
            case "A" -> 4;
            case "B" -> 3;
            case "C" -> 2;
            case "D" -> 1;
            default -> 0;
        };
    }
}
