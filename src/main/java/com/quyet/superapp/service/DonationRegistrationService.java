package com.quyet.superapp.service;

import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.mapper.DonationRegistrationMapper;
import com.quyet.superapp.repository.DonationRegistrationRepository;
import com.quyet.superapp.repository.UserProfileRepository;
import com.quyet.superapp.repository.UserRepository;
import com.quyet.superapp.repository.address.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationRegistrationService {

    private final DonationRegistrationRepository donationRegistrationRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final EmailService emailService; //
    private final AddressRepository addressRepository;

    // Đăng ký hiến máu
    public DonationRegistrationDTO register(Long userId, DonationRegistrationDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy User với ID: " + userId));

        // Kiểm tra trùng đăng ký (trạng thái đang chờ)
        if (donationRegistrationRepository.existsByUser_UserIdAndStatus(userId, DonationStatus.PENDING)) {
            throw new MemberException("DUPLICATE_PENDING", "Bạn đã có một đơn đăng ký đang chờ xác nhận.");
        }

        // ✅ Nếu chưa có UserProfile thì tạo mới
        if (user.getUserProfile() == null) {
            UserProfile profile = new UserProfile();
            profile.setUser(user);
            profile.setFullName(dto.getFullName());
            profile.setDob(dto.getDob());
            profile.setGender(dto.getGender());
            profile.setBloodType(dto.getBloodType());
            // ✅ Dùng addressId để lấy địa chỉ từ DB
            if (dto.getAddressId() != null) {
                Address address = addressRepository.findById(dto.getAddressId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ với ID: " + dto.getAddressId()));
                profile.setAddress(address);
            }
            profile.setPhone(dto.getPhone());
            userProfileRepository.save(profile);
        }

        // Tạo đơn đăng ký mới
        DonationRegistration registration = DonationRegistrationMapper.toEntity(dto, user);
        registration.setStatus(DonationStatus.PENDING);

        return DonationRegistrationMapper.toDTO(donationRegistrationRepository.save(registration));
    }

    // ✅ Lấy danh sách tất cả đơn đăng ký
    public List<DonationRegistrationDTO> getAllDTO() {
        return donationRegistrationRepository.findAll().stream()
                .map(DonationRegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Lấy đơn đăng ký theo ID
    public DonationRegistrationDTO getDTOById(Long id) {
        return donationRegistrationRepository.findById(id)
                .map(DonationRegistrationMapper::toDTO)
                .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy đơn đăng ký"));
    }
    // ✅ Xác nhận đơn đăng ký
    public DonationRegistrationDTO confirm(Long registrationId) {
        DonationRegistration reg = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("REGISTRATION_NOT_FOUND",
                        "Không tìm thấy đơn đăng ký có ID = " + registrationId));

        if (reg.getStatus() != DonationStatus.PENDING) {
            throw new MemberException("INVALID_STATUS", "Đơn đăng ký không ở trạng thái chờ xác nhận.");
        }

        reg.setStatus(DonationStatus.CONFIRMED);
        DonationRegistration saved = donationRegistrationRepository.save(reg);

        // ✅ Cập nhật UserProfile (nhóm máu, ngày hiến gần nhất...)
        User user = reg.getUser();
        UserProfile profile = user.getUserProfile();
        String bloodTypeFromReg = reg.getBloodType();

        if ((profile.getBloodType() == null || profile.getBloodType().isBlank()) &&
                bloodTypeFromReg != null && !bloodTypeFromReg.isBlank()) {
            profile.setBloodType(bloodTypeFromReg);
        }

        profile.setLastDonationDate(reg.getReadyDate());
        profile.setRecoveryTime(90);
        userProfileRepository.save(profile);

        // ✅ Gửi email xác nhận
        String subject = "Xác nhận hiến máu thành công";
        String content = String.format(
                "<h3>Chào %s</h3><p>Đơn đăng ký hiến máu của bạn vào ngày <b>%s</b> đã được xác nhận.</p><p>Cảm ơn bạn vì nghĩa cử cao đẹp!</p>",
                user.getUsername(), reg.getReadyDate());
        emailService.sendEmail(user, subject, content, "XÁC NHẬN");

        return DonationRegistrationMapper.toDTO(saved);
    }
}
