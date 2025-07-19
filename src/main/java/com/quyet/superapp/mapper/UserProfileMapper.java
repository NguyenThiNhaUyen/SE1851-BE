<<<<<<< HEAD
package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.UserProfileDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.entity.address.District;
import com.quyet.superapp.entity.address.City;

import java.time.format.DateTimeFormatter;

public class UserProfileMapper {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ✅ Entity → DTO
    public static UserProfileDTO toDTO(UserProfile profile) {
        UserProfileDTO dto = new UserProfileDTO();

        dto.setUserId(profile.getUser() != null ? profile.getUser().getUserId() : null);
        dto.setFullName(profile.getFullName());
        dto.setDob(profile.getDob());
        dto.setGender(profile.getGender());
        dto.setBloodTypeId(profile.getBloodType() != null ? profile.getBloodType().getBloodTypeId() : null);
        dto.setPhone(profile.getPhone());

        dto.setEmail(profile.getUser() != null ? profile.getUser().getEmail() : null);
        dto.setOccupation(profile.getOccupation());
        dto.setLastDonationDate(profile.getLastDonationDate());
        dto.setRecoveryTime(profile.getRecoveryTime());
        dto.setLocation(profile.getLocation());
        dto.setCitizenId(profile.getCitizenId());
        dto.setHeight(profile.getHeight());
        dto.setWeight(profile.getWeight());

        // ✅ Bảo hiểm y tế
        dto.setHasInsurance(profile.getHasInsurance());
        dto.setInsuranceCardNumber(profile.getInsuranceCardNumber());
        dto.setInsuranceValidTo(profile.getInsuranceValidTo());

        // ✅ Địa chỉ đầy đủ
        if (profile.getAddress() != null && profile.getAddress().getWard() != null) {
            Address addr = profile.getAddress();
            Ward ward = addr.getWard();
            District district = ward.getDistrict();
            City city = district.getCity();

            String fullAddress = String.format(
                    "%s, %s, %s, %s",
                    addr.getAddressStreet(),
                    ward.getWardName(),
                    district.getDistrictName(),
                    city.getNameCity()
            );
            dto.setAddressFull(fullAddress);
        }

        return dto;
    }

    // ✅ DTO → Entity
    public static UserProfile toEntity(UserProfileDTO dto, User user, Address address) {
        UserProfile profile = new UserProfile();

        profile.setUser(user);
        profile.setDob(dto.getDob());
        profile.setFullName(dto.getFullName());
        profile.setGender(dto.getGender());
        profile.setPhone(dto.getPhone());
        profile.setEmail(dto.getEmail());
        profile.setOccupation(dto.getOccupation());
        profile.setLastDonationDate(dto.getLastDonationDate());
        profile.setRecoveryTime(dto.getRecoveryTime());
        profile.setLocation(dto.getLocation());
        profile.setCitizenId(dto.getCitizenId());
        profile.setAddress(address);
        profile.setHeight(dto.getHeight());
        profile.setWeight(dto.getWeight());

        // ✅ Bảo hiểm y tế
        profile.setHasInsurance(dto.getHasInsurance());
        profile.setInsuranceCardNumber(dto.getInsuranceCardNumber());
        profile.setInsuranceValidTo(dto.getInsuranceValidTo());

        return profile;
    }
}
=======
    package com.quyet.superapp.mapper;

    import com.quyet.superapp.dto.UserProfileCreateDTO;
    import com.quyet.superapp.dto.UserProfileUpdateDTO;
    import com.quyet.superapp.dto.UserProfileResponseDTO;
    import com.quyet.superapp.entity.User;
    import com.quyet.superapp.entity.UserProfile;
    import com.quyet.superapp.entity.address.Address;
    import com.quyet.superapp.entity.address.City;
    import com.quyet.superapp.entity.address.District;
    import com.quyet.superapp.entity.address.Ward;

    public class UserProfileMapper {

        // ✅ Entity → ResponseDTO
        public static UserProfileResponseDTO toResponseDTO(UserProfile profile) {
            if (profile == null) return null;

            UserProfileResponseDTO dto = new UserProfileResponseDTO();
            dto.setUserId(profile.getUser() != null ? profile.getUser().getUserId() : null);
            dto.setFullName(profile.getFullName());
            dto.setDob(profile.getDob());
            dto.setGender(profile.getGender());
            dto.setBloodType(profile.getBloodType());
            dto.setPhone(profile.getPhone());
            dto.setEmail(profile.getEmail());
            dto.setOccupation(profile.getOccupation());
            dto.setLastDonationDate(profile.getLastDonationDate());
            dto.setRecoveryTime(profile.getRecoveryTime());
            dto.setLocation(profile.getLocation());
            dto.setCitizenId(profile.getCitizenId());
            dto.setWeight(profile.getWeightKg());
            dto.setHeight(profile.getHeightCm());
            dto.setLatitude(profile.getLatitude());
            dto.setLongitude(profile.getLongitude());
            dto.setEmergencyContact(profile.getEmergencyContact());
            dto.setAltPhone(profile.getAltPhone());

            // ✅ BHYT
            dto.setHasInsurance(profile.isHasInsurance());
            dto.setInsuranceCardNumber(profile.getInsuranceCardNumber());
            dto.setInsuranceValidTo(profile.getInsuranceValidTo());

            if (profile.getAddress() != null) {
                Address address = profile.getAddress();
                Ward ward = address.getWard();
                District district = ward.getDistrict();
                City city = district.getCity();

                String fullAddress = String.format(
                        "%s, %s, %s, %s",
                        address.getAddressStreet(),
                        ward.getWardName(),
                        district.getDistrictName(),
                        city.getNameCity()
                );

                dto.setAddressFull(fullAddress);
                dto.setAddressId(address.getAddressId());
            }

            return dto;
        }

        // ✅ CreateDTO → Entity
        public static UserProfile fromCreateDTO(UserProfileCreateDTO dto, User user, Address address) {
            UserProfile profile = new UserProfile();
            profile.setUser(user);
            profile.setFullName(dto.getFullName());
            profile.setDob(dto.getDob());
            profile.setGender(dto.getGender());
            profile.setPhone(dto.getContactInfo().getPhone());
            profile.setEmail(dto.getContactInfo().getEmail());
            profile.setOccupation(dto.getOccupation());
            profile.setCitizenId(dto.getCitizenId());
            profile.setEmergencyContact(dto.getContactInfo().getEmergencyContact());
            profile.setAltPhone(dto.getContactInfo().getAltPhone());
            profile.setWeightKg(dto.getWeight());
            profile.setHeightCm(dto.getHeight());
            profile.setAddress(address);

            // ✅ BHYT
            profile.setHasInsurance(dto.isHasInsurance());
            profile.setInsuranceCardNumber(dto.getInsuranceCardNumber());
            profile.setInsuranceValidTo(dto.getInsuranceValidTo());

            return profile;
        }

        // ✅ UpdateDTO → Entity
        public static void updateEntityFromDTO(UserProfile profile, UserProfileUpdateDTO dto, Address address) {
            if (dto.getFullName() != null) profile.setFullName(dto.getFullName());
            if (dto.getDob() != null) profile.setDob(dto.getDob());
            if (dto.getGender() != null) profile.setGender(dto.getGender());
            if (dto.getPhone() != null) profile.setPhone(dto.getPhone());
            if (dto.getEmail() != null) profile.setEmail(dto.getEmail());
            if (dto.getOccupation() != null) profile.setOccupation(dto.getOccupation());
            if (dto.getLastDonationDate() != null) profile.setLastDonationDate(dto.getLastDonationDate());
            if (dto.getRecoveryTime() != null) profile.setRecoveryTime(dto.getRecoveryTime());
            if (dto.getLocation() != null) profile.setLocation(dto.getLocation());
            if (dto.getCitizenId() != null) profile.setCitizenId(dto.getCitizenId());
            if (dto.getWeight() != null) profile.setWeightKg(dto.getWeight());
            if (dto.getHeight() != null) profile.setHeightCm(dto.getHeight());
            if (dto.getLatitude() != null) profile.setLatitude(dto.getLatitude());
            if (dto.getLongitude() != null) profile.setLongitude(dto.getLongitude());

            // ✅ BHYT
            if (dto.getHasInsurance() != null) profile.setHasInsurance(dto.getHasInsurance());
            if (dto.getInsuranceCardNumber() != null) profile.setInsuranceCardNumber(dto.getInsuranceCardNumber());
            if (dto.getInsuranceValidTo() != null) profile.setInsuranceValidTo(dto.getInsuranceValidTo());

            if (address != null) {
                profile.setAddress(address);
            }
        }
    }
>>>>>>> origin/main
