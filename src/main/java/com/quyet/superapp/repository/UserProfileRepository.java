package com.quyet.superapp.repository;

import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser_UserId(Long userId);

    // Tìm theo entity User
    Optional<UserProfile> findByUser(User user);

    // Tìm theo CCCD
    Optional<UserProfile> findByCitizenId(String citizenId);

    // Kiểm tra CCCD có tồn tại không
    boolean existsByCitizenId(String citizenId);

    // Kiểm tra email có tồn tại không
    boolean existsByEmail(String email);

    // Tìm theo email (dùng cho xác minh)
    Optional<UserProfile> findByEmail(String email);
}
