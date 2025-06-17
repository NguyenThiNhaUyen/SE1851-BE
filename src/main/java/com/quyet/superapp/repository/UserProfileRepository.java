package com.quyet.superapp.repository;

import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT DISTINCT up.location FROM UserProfile up " +
            "WHERE up.address.ward.wardId = :wardId " +
            "AND up.location IS NOT NULL " +
            "AND up.location LIKE %:keyword%")
    List<String> findSuggestedStreets(@Param("wardId") Long wardId, @Param("keyword") String keyword);

    Optional<UserProfile> findByUser(User user);

}
