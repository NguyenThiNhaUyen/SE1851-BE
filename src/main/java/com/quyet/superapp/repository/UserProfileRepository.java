package com.quyet.superapp.repository;

import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.time.LocalDate;
=======
>>>>>>> origin/main
import java.util.List;
import java.util.Optional;


<<<<<<< HEAD
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    boolean existsByInsuranceCardNumber(String insuranceCardNumber);


=======

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
>>>>>>> origin/main
    Optional<UserProfile> findByUser_UserId(Long userId);

    // Tìm theo entity User
    Optional<UserProfile> findByUser(User user);

<<<<<<< HEAD
    // Tìm theo CCCD
    Optional<UserProfile> findByCitizenId(String citizenId);
=======
>>>>>>> origin/main

    // Kiểm tra CCCD có tồn tại không
    boolean existsByCitizenId(String citizenId);

    // Kiểm tra email có tồn tại không
    boolean existsByEmail(String email);

<<<<<<< HEAD
    // Tìm theo email (dùng cho xác minh)
    Optional<UserProfile> findByEmail(String email);
=======
>>>>>>> origin/main

    @Query("SELECT DISTINCT up.location FROM UserProfile up " +
            "WHERE up.address.ward.wardId = :wardId " +
            "AND up.location IS NOT NULL " +
            "AND up.location LIKE %:keyword%")
    List<String> findSuggestedStreets(@Param("wardId") Long wardId, @Param("keyword") String keyword);


<<<<<<< HEAD
    boolean existsByPhone(String phone);
=======
>>>>>>> origin/main
}
