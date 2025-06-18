package com.quyet.superapp.service;

import com.quyet.superapp.dto.UrgentDonorRegistrationDTO;
import com.quyet.superapp.dto.UserProfileDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.mapper.AddressMapper;
import com.quyet.superapp.repository.UserProfileRepository;
import com.quyet.superapp.repository.UserRepository;
import com.quyet.superapp.repository.address.AddressRepository;
import com.quyet.superapp.repository.address.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final WardRepository wardRepository;

    // ✅ Lấy tất cả hồ sơ
    public List<UserProfile> getAllProfiles() {
        return userProfileRepository.findAll();
    }

    // ✅ Lấy hồ sơ theo userId
    public UserProfile getProfileByUserId(Long userId) {
        return userProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ người dùng với ID: " + userId));
    }

    // ✅ Tạo mới hồ sơ từ DTO
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

        updateEntityFromDTO(profile, dto);
        return userProfileRepository.save(profile);
    }

    // ✅ Tạo hồ sơ từ đăng ký người hiến khẩn cấp
    public UserProfile createFromRegistration(User user, UrgentDonorRegistrationDTO dto, Address address) {
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setFullName(dto.getFullName());
        profile.setDob(dto.getDob());
        profile.setGender(dto.getGender());
        profile.setPhone(dto.getPhone());
        profile.setLatitude(dto.getLatitude());
        profile.setLongitude(dto.getLongitude());
        profile.setLocation(dto.getLocation());
        profile.setAddress(address);
        return userProfileRepository.save(profile);
    }

    // ✅ Lấy hồ sơ theo username (dùng trong xác thực)
    public UserProfile getByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(userProfileRepository::findByUser)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy hồ sơ của người dùng: " + username));
    }

    // ✅ Lưu hồ sơ (dùng chung)
    public UserProfile save(UserProfile profile) {
        return userProfileRepository.save(profile);
    }

    // ✅ Xóa theo ID
    public void deleteById(Long id) {
        userProfileRepository.deleteById(id);
    }

    // ✅ Lấy theo ID
    public Optional<UserProfile> getById(Long id) {
        return userProfileRepository.findById(id);
    }

    // 🔧 Helper: Tạo entity từ DTO
    private UserProfile mapDTOtoEntity(UserProfileDTO dto, User user) {
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        updateEntityFromDTO(profile, dto);
        return profile;
    }

    // 🔧 Helper: Cập nhật entity từ DTO
    private void updateEntityFromDTO(UserProfile profile, UserProfileDTO dto) {
        profile.setFullName(dto.getFullName());
        profile.setDob(dto.getDob());
        profile.setGender(dto.getGender());
        profile.setBloodType(dto.getBloodType());

        // ✅ Ưu tiên addressId (dữ liệu chuẩn), fallback sang AddressDTO nếu cần
        if (dto.getAddressId() != null) {
            Address address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
            profile.setAddress(address);
        } else if (dto.getAddress() != null && dto.getAddress().getWardId() != null) {
            Ward ward = wardRepository.findById(dto.getAddress().getWardId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phường với ID: " + dto.getAddress().getWardId()));
            Address address = AddressMapper.toEntity(dto.getAddress(), ward);
            profile.setAddress(address);
        }

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
