package com.quyet.superapp.service;

import com.quyet.superapp.dto.UserProfileDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.repository.UserProfileRepository;
import com.quyet.superapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    // ✅ Lấy tất cả hồ sơ
    public List<UserProfile> getAllProfiles() {
        return userProfileRepository.findAll();
    }

    // ✅ Lấy hồ sơ theo userId
    public UserProfile getProfileByUserId(Long userId) {
        return userProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ người dùng với ID: " + userId));
    }

    // ✅ Tạo mới hồ sơ
    public UserProfile createProfile(Long userId, UserProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        if (userProfileRepository.existsByCitizenId(dto.getCitizenId())) {
            throw new IllegalArgumentException("CCCD đã tồn tại trong hệ thống");
        }

        if (userProfileRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại trong hệ thống");
        }

        if (user.getUserProfile() != null) {
            throw new IllegalStateException("Người dùng đã có hồ sơ. Vui lòng cập nhật.");
        }

        UserProfile profile = mapDTOtoEntity(dto, user);
        return userProfileRepository.save(profile);
    }

    // ✅ Cập nhật hồ sơ
    public UserProfile updateProfile(Long userId, UserProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
        }

        if (dto.getCitizenId() != null &&
                !dto.getCitizenId().equals(profile.getCitizenId()) &&
                userProfileRepository.existsByCitizenId(dto.getCitizenId())) {
            throw new IllegalArgumentException("CCCD đã tồn tại trong hệ thống");
        }

        if (dto.getEmail() != null &&
                !dto.getEmail().equals(profile.getEmail()) &&
                userProfileRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại trong hệ thống");
        }

        // Gán dữ liệu
        updateEntityFromDTO(profile, dto);
        return userProfileRepository.save(profile);
    }

    // 🔧 Dùng chung để map DTO sang Entity
    private UserProfile mapDTOtoEntity(UserProfileDTO dto, User user) {
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        updateEntityFromDTO(profile, dto);
        return profile;
    }

    private void updateEntityFromDTO(UserProfile profile, UserProfileDTO dto) {
        profile.setFullName(dto.getFullName());
        profile.setDob(dto.getDob());
        profile.setGender(dto.getGender());
        profile.setBloodType(dto.getBloodType());
        profile.setAddress(dto.getAddress());
        profile.setPhone(dto.getPhone());
        profile.setLandline(dto.getLandline());
        profile.setEmail(dto.getEmail());
        profile.setOccupation(dto.getOccupation());
        profile.setLastDonationDate(dto.getLastDonationDate());
        profile.setRecoveryTime(dto.getRecoveryTime());
        profile.setLocation(dto.getLocation());
        profile.setCitizenId(dto.getCitizenId());
    }
}
