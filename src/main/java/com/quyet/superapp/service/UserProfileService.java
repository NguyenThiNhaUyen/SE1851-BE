package com.quyet.superapp.service;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.mapper.AddressMapper;
import com.quyet.superapp.mapper.UserProfileMapper;
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

    /**
     * Lấy tất cả hồ sơ người dùng
     */
    public List<UserProfile> getAllProfiles() {
        return userProfileRepository.findAll();
    }

    /**
     * Lấy hồ sơ theo ID người dùng
     */
    public UserProfile getProfileByUserId(Long userId) {
        return userProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ người dùng với ID: " + userId));
    }

    /**
     * Tạo hồ sơ người dùng mới
     */
    public UserProfile createProfile(Long userId, UserProfileCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId));

        if (user.getUserProfile() != null) {
            throw new IllegalStateException("Người dùng đã có hồ sơ. Vui lòng cập nhật.");
        }

        validateUniqueFields(dto.getCitizenId(), dto.getContactInfo().getEmail(), null);
        Address address = resolveAddress(dto.getAddressId(), dto.getAddress());
        UserProfile profile = UserProfileMapper.fromCreateDTO(dto, user, address);
        return userProfileRepository.save(profile);
    }

    /**
     * Cập nhật hồ sơ người dùng
     */
    public UserProfile updateProfile(Long userId, UserProfileUpdateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId));

        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new IllegalStateException("Hồ sơ không tồn tại. Vui lòng tạo mới.");
        }

        validateUniqueFields(dto.getCitizenId(), dto.getEmail(), profile);
        Address address = resolveAddress(dto.getAddressId(), dto.getAddress());
        UserProfileMapper.updateEntityFromDTO(profile, dto, address);
        return userProfileRepository.save(profile);
    }

    /**
     * Tạo hồ sơ từ đăng ký khẩn cấp
     */
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

    /**
     * Lấy hồ sơ từ username
     */
    public UserProfile getByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(userProfileRepository::findByUser)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy hồ sơ người dùng: " + username));
    }

    /**
     * Lưu hồ sơ
     */
    public UserProfile save(UserProfile profile) {
        return userProfileRepository.save(profile);
    }

    /**
     * Xoá hồ sơ theo ID
     */
    public void deleteById(Long id) {
        userProfileRepository.deleteById(id);
    }

    /**
     * Lấy hồ sơ theo ID
     */
    public Optional<UserProfile> getById(Long id) {
        return userProfileRepository.findById(id);
    }

    /**
     * Tạo hoặc cập nhật hồ sơ từ đăng ký hiến máu
     */
    public void updateOrCreateFromRegistration(User user, DonationRegistrationDTO dto) {
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
        }

        profile.setFullName(dto.getFullName());
        profile.setDob(dto.getDob());
        profile.setGender(dto.getGender());
        profile.setPhone(dto.getPhone());
        profile.setBloodType(dto.getBloodType());

        if (dto.getAddressId() != null) {
            Address address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ"));
            profile.setAddress(address);
        }

        userProfileRepository.save(profile);
    }

    // 🔽 Xử lý địa chỉ
    private Address resolveAddress(Long addressId, AddressDTO dto) {
        if (addressId != null) {
            return addressRepository.findById(addressId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ"));
        } else if (dto != null && dto.getWardId() != null) {
            Ward ward = wardRepository.findById(dto.getWardId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phường/xã"));
            return AddressMapper.toEntity(dto, ward);
        } else {
            throw new IllegalArgumentException("Địa chỉ không hợp lệ hoặc thiếu thông tin");
        }
    }

    // 🔽 Kiểm tra trùng CCCD và Email
    private void validateUniqueFields(String citizenId, String email, UserProfile currentProfile) {
        if (citizenId != null &&
                (currentProfile == null || !citizenId.equals(currentProfile.getCitizenId())) &&
                userProfileRepository.existsByCitizenId(citizenId)) {
            throw new IllegalArgumentException("CCCD đã tồn tại trong hệ thống");
        }

        if (email != null &&
                (currentProfile == null || !email.equals(currentProfile.getEmail())) &&
                userProfileRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email đã tồn tại trong hệ thống");
        }
    }
}
