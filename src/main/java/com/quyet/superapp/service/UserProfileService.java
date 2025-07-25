package com.quyet.superapp.service;


import com.quyet.superapp.dto.UrgentDonorRegistrationDTO;
import com.quyet.superapp.entity.BloodType;

import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
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

    public List<UserProfile> getAllProfiles() {
        return userProfileRepository.findAll();
    }

    public UserProfile getProfileByUserId(Long userId) {
        return userProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ người dùng với ID: " + userId));
    }



    // ✅ Tạo mới hồ sơ từ DTO
    @Transactional
    public UserProfile createProfile(Long userId, UserProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        validateUniqueFields(dto.getCitizenId(), dto.getEmail(), null);

        public UserProfile createProfile(Long userId, UserProfileCreateDTO dto) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

    public UserProfile createProfile(Long userId, UserProfileCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));


            if (user.getUserProfile() != null) {
                throw new IllegalStateException("Người dùng đã có hồ sơ. Vui lòng cập nhật.");
            }



            UserProfile profile = mapDTOtoEntity(dto, user);
            return userProfileRepository.save(profile);

        }

        // ✅ Cập nhật hồ sơ
        public UserProfile updateProfile(Long userId, UserProfileDTO dto) {

            validateUniqueFields(dto.getCitizenId(), dto.getContactInfo().getEmail(), null);
            Address address = resolveAddress(dto.getAddressId(), dto.getAddress());
            UserProfile profile = UserProfileMapper.fromCreateDTO(dto, user, address);
            return userProfileRepository.save(profile);
        }

        validateUniqueFields(dto.getCitizenId(), dto.getContactInfo().getEmail(), null);
        Address address = resolveAddress(dto.getAddressId(), dto.getAddress());
        UserProfile profile = UserProfileMapper.fromCreateDTO(dto, user, address);
        return userProfileRepository.save(profile);
    }


        public UserProfile updateProfile(Long userId, UserProfileUpdateDTO dto) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));


            UserProfile profile = user.getUserProfile();
            if (profile == null) {

                profile = new UserProfile();
                profile.setUser(user);
            }

            validateUniqueFields(dto.getCitizenId(), dto.getEmail(), profile);

            updateEntityFromDTO(profile, dto);


            throw new IllegalStateException("Hồ sơ không tồn tại. Vui lòng tạo mới.");
        }

        validateUniqueFields(dto.getCitizenId(), dto.getEmail(), profile);
        Address address = resolveAddress(dto.getAddressId(), dto.getAddress());
        UserProfileMapper.updateEntityFromDTO(profile, dto, address);
        return userProfileRepository.save(profile);
    }



        if (user == null) {
            throw new IllegalArgumentException("User không được null khi tạo UserProfile");
        }

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setLatitude(dto.getLatitude());
        profile.setLongitude(dto.getLongitude());
        profile.setAddress(address);

        // Gán nhóm máu nếu có
        if (dto.getBloodTypeId() != null) {
            BloodType bloodType = bloodTypeRepository.findById(dto.getBloodTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhóm máu với ID: " + dto.getBloodTypeId()));
            profile.setBloodType(bloodType);
        }

        return userProfileRepository.save(profile);
    }


    // ✅ Lấy hồ sơ theo username (dùng trong xác thực)

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

    }

    public UserProfile getByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(userProfileRepository::findByUser)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy hồ sơ của người dùng: " + username));
    }



public UserProfile getByUsername(String username) {
    return userRepository.findByUsername(username)
            .flatMap(userProfileRepository::findByUser)
            .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy hồ sơ của người dùng: " + username));
}

public UserProfile save(UserProfile profile) {
    return userProfileRepository.save(profile);
}


public void deleteById(Long id) {
    userProfileRepository.deleteById(id);
}

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
    if (dto.getFullName() == null || dto.getFullName().isBlank()) {
        throw new IllegalArgumentException("Họ tên không được để trống");

    // ✅ Tách xử lý địa chỉ dùng chung
    private Address resolveAddress(Long addressId, AddressDTO dto) {
        if (addressId != null) {
            return addressRepository.findById(addressId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
        } else if (dto != null && dto.getWardId() != null) {
            Ward ward = wardRepository.findById(dto.getWardId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phường/xã"));
            return AddressMapper.toEntity(dto, ward);
        } else {
            throw new IllegalArgumentException("Địa chỉ không hợp lệ hoặc thiếu thông tin")
    }

    if (dto.getCitizenId() == null || !dto.getCitizenId().matches("\\d{12}")) {
        throw new IllegalArgumentException("CCCD không hợp lệ (phải gồm 12 chữ số)");
    }

    if (dto.getDob() == null) {
        throw new IllegalArgumentException("Ngày sinh không được để trống");

        // ✅ Tách xử lý địa chỉ dùng chung
        private Address resolveAddress (Long addressId, AddressDTO dto){
            if (addressId != null) {
                return addressRepository.findById(addressId)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
            } else if (dto != null && dto.getWardId() != null) {
                Ward ward = wardRepository.findById(dto.getWardId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy phường/xã"));
                return AddressMapper.toEntity(dto, ward);
            } else {
                throw new IllegalArgumentException("Địa chỉ không hợp lệ hoặc thiếu thông tin");
            }
        }

        private void validateUniqueFields (String citizenId, String email, UserProfile currentProfile){
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

        public void updateOrCreateFromRegistration (User user, DonationRegistrationDTO dto){
            UserProfile profile = user.getUserProfile();
            if (profile == null) {
                profile = new UserProfile();
                profile.setUser(user);

            }

            profile.setFullName(dto.getFullName());
            profile.setDob(dto.getDob());
            profile.setGender(dto.getGender());

            profile.setPhone(dto.getPhone());

            profile.setEmail(dto.getEmail());
            profile.setLastDonationDate(dto.getLastDonationDate());
            profile.setRecoveryTime(dto.getRecoveryTime());
            profile.setLocation(dto.getLocation());
            profile.setCitizenId(dto.getCitizenId());
            profile.setOccupation(dto.getOccupation());
            profile.setWeight(dto.getWeight());
            profile.setHeight(dto.getHeight());
            profile.setLatitude(dto.getLatitude());
            profile.setLongitude(dto.getLongitude());

            // ✅ Gán BloodType nếu có
            if (dto.getBloodTypeId() != null) {
                BloodType bloodType = bloodTypeRepository.findById(dto.getBloodTypeId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm máu với ID: " + dto.getBloodTypeId()));
                profile.setBloodType(bloodType);
            } else {
                profile.setBloodType(null); // hoặc giữ nguyên nếu muốn tránh xoá dữ liệu cũ
            }

            // ✅ Ưu tiên addressId → fallback sang AddressDTO nếu có
            if (dto.getAddressId() != null) {
                Address address = addressRepository.findById(dto.getAddressId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ với ID: " + dto.getAddressId()));
                profile.setAddress(address);
            } else if (dto.getAddress() != null && dto.getAddress().getWardId() != null) {
                Ward ward = wardRepository.findById(dto.getAddress().getWardId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy phường với ID: " + dto.getAddress().getWardId()));
                Address address = addressMapper.toEntity(dto.getAddress());
                profile.setAddress(address);
            } else {
                throw new IllegalArgumentException("Địa chỉ không hợp lệ hoặc chưa được chọn đầy đủ");
            }
        }

        private void validateUniqueFields (String citizenId, String email, UserProfile currentProfile){
            if (citizenId != null && (currentProfile == null || !citizenId.equals(currentProfile.getCitizenId())) && userProfileRepository.existsByCitizenId(citizenId)) {
                throw new IllegalArgumentException("CCCD đã tồn tại trong hệ thống");
            }

            if (email != null && (currentProfile == null || !email.equals(currentProfile.getEmail())) && userProfileRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("Email đã tồn tại trong hệ thống");
            }
        }
    }

    profile.setBloodType(dto.getBloodType());

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
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
            profile.setAddress(address);
        }


    if (dto.getAddressId() != null) {
        Address address = addressRepository.findById(dto.getAddressId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
        profile.setAddress(address);
    }

    userProfileRepository.save(profile);
}
}

