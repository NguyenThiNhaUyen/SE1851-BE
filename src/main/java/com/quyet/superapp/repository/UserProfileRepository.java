package com.quyet.superapp.repository;

import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser_UserId(Long userId);

    // Tìm theo entity User
    Optional<UserProfile> findByUser(User user);


    // Kiểm tra CCCD có tồn tại không
    boolean existsByCitizenId(String citizenId);

    // Kiểm tra email có tồn tại không
    boolean existsByEmail(String email);


    @Query("SELECT DISTINCT up.location FROM UserProfile up " +
            "WHERE up.address.ward.wardId = :wardId " +
            "AND up.location IS NOT NULL " +
            "AND up.location LIKE %:keyword%")
    List<String> findSuggestedStreets(@Param("wardId") Long wardId, @Param("keyword") String keyword);


}
