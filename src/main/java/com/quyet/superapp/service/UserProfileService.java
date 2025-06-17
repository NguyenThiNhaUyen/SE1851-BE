package com.quyet.superapp.service;

import com.quyet.superapp.dto.UrgentDonorRegistrationDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.repository.UserProfileRepository;
import com.quyet.superapp.repository.UserRepository;
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

    /**
     * Tạo hồ sơ người dùng từ form đăng ký người hiến khẩn cấp
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

    public List<UserProfile> getAll() {
        return userProfileRepository.findAll();
    }

    public Optional<UserProfile> getById(Long id) {
        return userProfileRepository.findById(id);
    }

    public UserProfile save(UserProfile profile) {
        return userProfileRepository.save(profile);
    }

    public void deleteById(Long id) {
        userProfileRepository.deleteById(id);
    }

    /**
     * Lấy UserProfile dựa vào username từ JWT
     */
    public UserProfile getByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(userProfileRepository::findByUser)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy hồ sơ của người dùng: " + username));
    }
}
