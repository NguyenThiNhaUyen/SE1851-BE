package com.quyet.superapp.service;

import com.quyet.superapp.dto.UrgentDonorRegistrationDTO;
import com.quyet.superapp.dto.UrgentDonorResponseDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UrgentDonorRegistryService {

    private final UserRepository userRepository;
    private final BloodTypeRepository bloodTypeRepository;
    private final UrgentDonorRegistryRepository urgentDonorRegistryRepository;
    private final UserProfileRepository userProfileRepository;
    private final AddressService addressService;

    /**
     * Đăng ký người hiến máu khẩn cấp.
     */
    public void registerUrgentDonor(UrgentDonorRegistrationDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        BloodType bloodType = bloodTypeRepository.findById(dto.getBloodTypeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm máu"));

        Address address = addressService.createAddressFromDTO(dto.getAddressRequest());

        // Tạo bản ghi khẩn cấp
        UrgentDonorRegistry donor = new UrgentDonorRegistry();
        donor.setDonor(user);
        donor.setBloodType(bloodType);
        donor.setLocation(dto.getLocation());
        donor.setIsAvailable(true);
        donor.setLastContacted(null);
        urgentDonorRegistryRepository.save(donor);

        // Cập nhật hồ sơ
        UserProfile profile = user.getUserProfile();
        if (profile != null) {
            profile.setAddress(address);
            profile.setLatitude(dto.getLatitude());
            profile.setLongitude(dto.getLongitude());
            userProfileRepository.save(profile);
        }
    }

    public List<UrgentDonorRegistry> getAllAvailableDonors() {
        return urgentDonorRegistryRepository.findAvailableDonorsAll();
    }

    public List<UrgentDonorRegistry> findNearbyDonors(double lat, double lng, double radiusKm) {
        return urgentDonorRegistryRepository.findNearbyDonors(lat, lng, radiusKm);
    }

    public List<UrgentDonorResponseDTO> filterDonorsByBloodTypeAndDistance(Long bloodTypeId, double lat, double lng, double radiusKm) {
        return urgentDonorRegistryRepository.findNearbyDonors(lat, lng, radiusKm)
                .stream()
                .filter(d -> d.getBloodType().getBloodTypeId().equals(bloodTypeId))
                .map(d -> {
                    UserProfile profile = d.getDonor().getUserProfile();
                    return new UrgentDonorResponseDTO(
                            d.getDonor().getUserId(),
                            profile.getFullName(),
                            d.getBloodType().getDescription(),
                            d.getLocation(),
                            profile.getPhone(),
                            profile.getAddress()

                    );
                })
                .toList();
    }
}
