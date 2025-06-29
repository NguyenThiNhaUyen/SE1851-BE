package com.quyet.superapp.repository;

import com.quyet.superapp.dto.SeparationVolumeStatsDTO;
import com.quyet.superapp.entity.BloodSeparationDetail;
import com.quyet.superapp.enums.BloodComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BloodSeparationDetailRepository extends JpaRepository<BloodSeparationDetail, Long> {

    // Các phương thức tìm kiếm khác
    List<BloodSeparationDetail> findByResult_SeparationResultId(Long resultId);
    List<BloodSeparationDetail> findByResult_Order_BloodBag_BagCode(String bagCode);
    List<BloodSeparationDetail> findByComponentType(BloodComponentType componentType);

    // Native Query cho Volume Stats, hỗ trợ định dạng ngày
    @Query(value = "SELECT new com.quyet.superapp.dto.SeparationVolumeStatsDTO(" +
            "FORMAT(r.completedAt, 'yyyy-MM-dd'), d.componentType, SUM(d.volumeMl)) " +
            "FROM BloodSeparationDetail d JOIN d.result r " +
            "GROUP BY FORMAT(r.completedAt, 'yyyy-MM-dd'), d.componentType", nativeQuery = true)
    List<SeparationVolumeStatsDTO> getVolumeStats(@Param("format") String format);

    // Native Query cho Volume Stats trong khoảng thời gian, hỗ trợ định dạng ngày
    @Query(value = "SELECT new com.quyet.superapp.dto.SeparationVolumeStatsDTO(" +
            "FORMAT(r.completedAt, 'yyyy-MM-dd'), d.componentType, SUM(d.volumeMl)) " +
            "FROM BloodSeparationDetail d JOIN d.result r " +
            "WHERE r.completedAt BETWEEN :start AND :end " +
            "GROUP BY FORMAT(r.completedAt, 'yyyy-MM-dd'), d.componentType", nativeQuery = true)
    List<SeparationVolumeStatsDTO> getVolumeStatsByDayRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
