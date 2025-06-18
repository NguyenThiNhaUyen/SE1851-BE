package com.quyet.superapp.repository;

import com.quyet.superapp.entity.UrgentDonorRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrgentDonorRegistryRepository extends JpaRepository<UrgentDonorRegistry, Long> {

    // ✅ 1. Tìm theo nhóm máu
    @Query("SELECT u FROM UrgentDonorRegistry u WHERE u.bloodType.bloodTypeId = :bloodTypeId AND u.isAvailable = true ORDER BY u.lastContacted ASC")
    List<UrgentDonorRegistry> findAvailableDonors(@Param("bloodTypeId") Long bloodTypeId);

    // ✅ 2. Lấy tất cả người đang sẵn sàng
    @Query("SELECT u FROM UrgentDonorRegistry u WHERE u.isAvailable = true")
    List<UrgentDonorRegistry> findAvailableDonorsAll();

    // ✅ 3. Lọc người gần vị trí (Haversine Formula)
    @Query("SELECT u FROM UrgentDonorRegistry u JOIN u.donor.userProfile p " +
            "WHERE u.isAvailable = true AND " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(p.latitude)))) < :radius")
    List<UrgentDonorRegistry> findNearbyDonors(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusKm
    );
}
