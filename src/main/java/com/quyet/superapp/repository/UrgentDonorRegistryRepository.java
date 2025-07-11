package com.quyet.superapp.repository;

import com.quyet.superapp.entity.UrgentDonorRegistry;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.enums.DonorReadinessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrgentDonorRegistryRepository extends JpaRepository<UrgentDonorRegistry, Long> {

    @Query("""
        SELECT d.donor FROM UrgentDonorRegistry d
        WHERE d.readinessLevel = :level AND d.isAvailable = true AND d.isVerified = true
    """)
    List<User> findUrgentDonorsByLevel(@Param("level") DonorReadinessLevel level);

    List<UrgentDonorRegistry> findAllByIsVerifiedTrueAndIsAvailableTrue();

    @Query("SELECT r FROM UrgentDonorRegistry r WHERE r.isAvailable = false")
    List<UrgentDonorRegistry> findUnavailableDonors();

    List<UrgentDonorRegistry> findByIsVerifiedTrueAndIsAvailableTrue();
    List<UrgentDonorRegistry> findByIsVerifiedFalse();

    Optional<UrgentDonorRegistry> findByDonor_UserIdAndIsVerifiedTrue(Long userId);

    @Query("""
        SELECT u.donor FROM UrgentDonorRegistry u
        WHERE u.isVerified = true AND u.isAvailable = true
        AND (u.readinessLevel = 'EMERGENCY_NOW' OR u.readinessLevel = 'EMERGENCY_FLEXIBLE')
    """)
    List<User> findVerifiedUrgentDonorsReady();

    @Query("""
        SELECT u FROM UrgentDonorRegistry u
        WHERE (u.isVerified IS NULL OR u.isVerified = false)
    """)
    List<UrgentDonorRegistry> findUnverifiedDonors();

    Optional<UrgentDonorRegistry> findByDonor_UserId(Long userId);

    @Query("SELECT u FROM UrgentDonorRegistry u WHERE u.bloodType.bloodTypeId = :bloodTypeId AND u.isAvailable = true ORDER BY u.lastContacted ASC")
    List<UrgentDonorRegistry> findAvailableDonors(@Param("bloodTypeId") Long bloodTypeId);

    @Query("SELECT u FROM UrgentDonorRegistry u WHERE u.isAvailable = true")
    List<UrgentDonorRegistry> findAvailableDonorsAll();

    @Query("""
        SELECT u FROM UrgentDonorRegistry u JOIN u.donor.userProfile p 
        WHERE u.isAvailable = true AND 
        (6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(p.latitude)))) < :radius
    """)
    List<UrgentDonorRegistry> findNearbyDonors(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusKm
    );

    Optional<UrgentDonorRegistry> findByDonor(User donor);

    boolean existsByDonor_UserId(Long userId);

    List<UrgentDonorRegistry> findByReadinessLevel(DonorReadinessLevel level); // ✅ Đúng tên field

    @Query("SELECT u FROM UrgentDonorRegistry u WHERE u.isVerified = true AND u.isAvailable = true")
    List<UrgentDonorRegistry> findAllVerified();
}
