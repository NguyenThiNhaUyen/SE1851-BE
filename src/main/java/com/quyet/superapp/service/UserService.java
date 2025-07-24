package com.quyet.superapp.service;

import com.quyet.superapp.config.jwt.JwtTokenProvider;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.enums.RoleEnum;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.exception.MultiFieldException;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.mapper.AddressMapper;
import com.quyet.superapp.mapper.UserMapper;
import com.quyet.superapp.repository.*;
import com.quyet.superapp.repository.address.AddressRepository;
import com.quyet.superapp.repository.address.WardRepository;
import jakarta.transaction.Transactional;

import com.quyet.superapp.config.jwt.UserPrincipal;
import com.quyet.superapp.constant.MessageConstants;
import com.quyet.superapp.dto.*;
import com.quyet.superapp.entity.Role;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.enums.EmailType;
import com.quyet.superapp.repository.*;
import com.quyet.superapp.repository.address.*;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;

import java.time.temporal.ChronoUnit;
import java.util.*;


import static com.quyet.superapp.constant.MessageConstants.LOGIN_FAILED;
import static com.quyet.superapp.constant.MessageConstants.LOGIN_SUCCESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AddressRepository addressRepository;
    private final WardRepository wardRepository;
    private final UserProfileRepository userProfileRepository;

    private final AddressMapper addressMapper;
    private final DonorProfileRepository donorProfileRepository;

    // ✅ Đăng nhập
    public ResponseEntity<?> login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()

    private final RedisOtpService redisOtpService;
    private final EmailService emailService;

    public ResponseEntity<ApiResponseDTO<?>> login(LoginRequestDTO loginRequest) {
        log.info("🎯 Đăng nhập với username: {}", loginRequest.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()

                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUsername(loginRequest.getUsername())

                    .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

            String jwt = tokenProvider.createToken(user.getUsername(), user.getUserId());

            LoginResponseDTO loginResponse = new LoginResponseDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().getName().toString(),
                    user.isEnable(),
                    jwt
            );
            return ResponseEntity.ok(loginResponse);

        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Tài khoản hoặc mật khẩu không đúng");
        }
    }


    private void applyInsuranceInfo(UserProfile profile, Boolean hasInsurance, String cardNumber, LocalDate validTo) {
        profile.setHasInsurance(hasInsurance);
        profile.setInsuranceCardNumber(cardNumber);
        profile.setInsuranceValidTo(validTo);
    }


    // ✅ Đăng ký thành viên (Member)
    @Transactional
    public ResponseEntity<?> register(RegisterRequestDTO request) {
        // 1. Kiểm tra thông tin trùng lặp
        validateRegisterFields(request);


        // 🔍 Kiểm tra nếu có BHYT thì phải nhập đúng thông tin
        if (Boolean.TRUE.equals(request.getHasInsurance())) {
            if (request.getInsuranceCardNumber() == null || request.getInsuranceCardNumber().isBlank()) {
                throw new MemberException("MISSING_BHYT_NUMBER", "Vui lòng nhập số thẻ BHYT");
            }
            if (request.getInsuranceValidTo() == null) {
                throw new MemberException("MISSING_BHYT_DATE", "Vui lòng nhập ngày hết hạn BHYT");
            }
            if (request.getInsuranceValidTo() != null && request.getInsuranceValidTo().isBefore(LocalDate.now())) {
                throw new MemberException("EXPIRED_BHYT", "Thẻ BHYT đã hết hạn");
            }

        }



        // 2. Tìm vai trò (mặc định là MEMBER)
        String roleName = Optional.ofNullable(request.getRole()).map(String::toUpperCase).orElse("MEMBER");
        RoleEnum roleEnum = RoleEnum.valueOf(roleName);
        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò " + roleName));

        // 3. Tạo User
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnable(true);
        user.setRole(role);

        // 4. Tạo Address nếu có
        Address address = null;
        if (request.getAddress() != null && request.getAddress().getWardId() != null) {
            Ward ward = wardRepository.findById(request.getAddress().getWardId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phường/xã với ID: " + request.getAddress().getWardId()));

            address = new Address();
            address.setAddressStreet(request.getAddress().getAddressStreet());
            address.setWard(ward);
            addressRepository.save(address);
        }

        // 5. Tạo UserProfile
        UserProfile profile = new UserProfile();
        applyInsuranceInfo(profile,
                request.getHasInsurance(),
                request.getInsuranceCardNumber(),
                request.getInsuranceValidTo());
        profile.setUser(user);
        profile.setFullName(request.getFirstName() + " " + request.getLastName());
        profile.setDob(request.getDob());
        profile.setGender(request.getGender());
        profile.setPhone(request.getPhone());
        profile.setCitizenId(request.getCitizenId());
        profile.setAddress(address);

        user.setUserProfile(profile);

        // 6. Lưu User (cascades profile)
        userRepository.save(user);
        log.info("✅ Tạo user mới thành công: {} ({})", user.getUsername(), roleEnum.name());

        // 7. Nếu là MEMBER → tạo DonorProfile
        if (roleEnum == RoleEnum.MEMBER) {
            DonorProfile donorProfile = new DonorProfile();
            donorProfile.setUser(user);
            donorProfileRepository.save(donorProfile);
            log.info("✅ DonorProfile đã tạo cho userId = {}", user.getUserId());
        }

        // 8. Trả về phản hồi
        return ResponseEntity.ok(Map.of(
                "message", "Đăng ký thành công",
                "userId", user.getUserId(),
                "username", user.getUsername(),
                "role", roleEnum.name()
        ));
    }


    // ✅ Hàm validate trùng lặp cho Member đăng ký
    private void validateRegisterFields(RegisterRequestDTO request) {
        Map<String, String> errors = new HashMap<>();

        if (userRepository.existsByUsername(request.getUsername())) {
            errors.put("username", "Tên đăng nhập đã tồn tại");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            errors.put("email", "Email đã tồn tại");
        }

        if (userProfileRepository.existsByPhone(request.getPhone())) {
            errors.put("phone", "SĐT đã tồn tại");
        }

        if (request.getCitizenId() != null) {
            String normalizedCitizenId = request.getCitizenId().trim(); // ✅ chuẩn hóa
            if (userProfileRepository.existsByCitizenId(normalizedCitizenId)) {
                errors.put("citizenId", "CCCD đã tồn tại");
            }
        }
        if (request.getInsuranceCardNumber() != null) {
            String normalizedInsurance = request.getInsuranceCardNumber().trim();
            if (userProfileRepository.existsByInsuranceCardNumber(normalizedInsurance)) {
                errors.put("insuranceCardNumber", "Số thẻ BHYT đã tồn tại");
            }
        }


        if (!errors.isEmpty()) {
            log.warn("❌ Đăng ký thất bại do trùng lặp: {}", errors); // ✅ Logging rõ ràng
            throw new MultiFieldException("Đăng ký tài khoản thất bại", errors);
        }
    }


    // ✅ Lấy danh sách bác sĩ
    public List<UserDTO> getAllDoctors() {
        return userRepository.findDoctors().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Admin tạo tài khoản staff/doctor
    @Transactional
    public void createUserByAdmin(AdminCreateUserRequestDTO dto) {
        Map<String, String> errors = new HashMap<>();

        if (userRepository.existsByUsername(dto.getUsername())) {
            errors.put("username", "Tên đăng nhập đã tồn tại");
        }
        if (userRepository.existsByEmail(dto.getContactInfo().getEmail())) {
            errors.put("email", "Email đã tồn tại");
        }
        if (userProfileRepository.existsByCitizenId(dto.getCitizenId())) {
            errors.put("citizenId", "CCCD đã tồn tại");
        }
        if (userProfileRepository.existsByPhone(dto.getContactInfo().getPhone())) {
            errors.put("phone", "SĐT đã tồn tại");
        }

        if (!errors.isEmpty()) {
            throw new MultiFieldException("Tạo tài khoản thất bại", errors);
        }

        String roleName = (dto.getStaffPosition() == null || dto.getStaffPosition().isBlank()) ? "MEMBER" : "STAFF";
        RoleEnum roleEnum = RoleEnum.valueOf(roleName);
        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new ResourceNotFoundException("Vai trò " + roleName + " không tồn tại"));

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getContactInfo().getEmail());
        user.setEnable(true);
        user.setRole(role);

        Address address = null;
        if (dto.getAddress() != null) {
            address = addressMapper.toEntity(dto.getAddress());
            addressRepository.save(address);
        }

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setFullName(dto.getFullName());
        profile.setDob(dto.getDob());
        profile.setGender(dto.getGender());
        profile.setPhone(dto.getContactInfo().getPhone());
        profile.setEmail(dto.getContactInfo().getEmail());
        profile.setCitizenId(dto.getCitizenId());
        profile.setWeight(dto.getWeight());
        profile.setHeight(dto.getHeight());
        profile.setLocation(dto.getLocation());
        profile.setStaffPosition(dto.getStaffPosition());
        profile.setAddress(address);

        user.setUserProfile(profile);
        userRepository.save(user);

        if ("MEMBER".equalsIgnoreCase(roleName)) {
            DonorProfile donorProfile = new DonorProfile();
            donorProfile.setUser(user);
            donorProfileRepository.save(donorProfile);
        }
    }

    public List<UserDTO> getAllStaffAndDoctors() {
        return userRepository.findStaffAndDoctors()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getByStaffPosition(String staffPosition) {
        return userRepository.findByStaffPosition(staffPosition)
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            LoginResponseDTO loginResponse = buildLoginResponse(user);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, LOGIN_SUCCESS , loginResponse));

        } catch (AuthenticationException e) {
            log.warn("⚠️ Đăng nhập thất bại với username [{}]: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(401).body(new ApiResponseDTO<>(false, LOGIN_FAILED ));
        } catch (Exception e) {
            log.error("❌ Lỗi không xác định khi đăng nhập [{}]: {}", loginRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, "Lỗi hệ thống khi đăng nhập"));
        }
    }

    public ResponseEntity<ApiResponseDTO<?>> logout(UserPrincipal principal) {
        log.info("👋 Người dùng {} đã logout", principal.getUsername());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Đăng xuất thành công"));
    }

    @Transactional
    public ResponseEntity<ApiResponseDTO<?>> register(@Valid RegisterRequestDTO request) {
        try {
            var contact = request.getContactInfo();
            if (contact == null)
                return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, "Thiếu thông tin liên hệ"));

            if (userRepository.existsByUsername(request.getUsername()))
                return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, MessageConstants.USERNAME_EXISTS));

            if (userRepository.existsByEmail(contact.getEmail()))
                return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, MessageConstants.EMAIL_EXISTS));

            if (userProfileRepository.existsByCitizenId(request.getCccd()))
                return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, MessageConstants.CCCD_EXISTS));
            if (userProfileRepository.existsByEmail(contact.getEmail()))
                return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, MessageConstants.EMAIL_PROFILE_EXISTS));

            if (request.getDob() != null && ChronoUnit.YEARS.between(request.getDob(), LocalDate.now()) < 18)
                return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, "Người dùng phải từ 18 tuổi trở lên"));

            Role role = roleRepository.findByName(
                            Optional.ofNullable(request.getRole()).map(String::toUpperCase).orElse("MEMBER"))
                    .orElseThrow(() -> new RuntimeException(MessageConstants.ROLE_NOT_FOUND));

            Address address = null;
            if (request.getAddress() != null && request.getAddress().getWardId() != null) {
                Ward ward = wardRepository.findById(request.getAddress().getWardId())
                        .orElseThrow(() -> new RuntimeException(MessageConstants.WARD_NOT_FOUND));
                address = new Address();
                address.setWard(ward);
                address.setAddressStreet(request.getAddress().getAddressStreet());
                addressRepository.save(address);
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(contact.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEnable(true); // ✅ Có thể thay bằng false nếu bạn muốn xác thực email sau
            user.setRole(role);
            userRepository.save(user);

            UserProfile profile = new UserProfile();
            profile.setUser(user);
            profile.setFullName(request.getFullName());
            profile.setDob(request.getDob());
            profile.setGender(request.getGender());
            profile.setPhone(contact.getPhone());
            profile.setAltPhone(contact.getAltPhone());
            profile.setEmail(contact.getEmail());
            profile.setEmergencyContact(contact.getEmergencyContact());
            profile.setCitizenId(request.getCccd());
            profile.setOccupation(request.getOccupation());
            profile.setWeightKg(request.getWeight());
            profile.setHeightCm(request.getHeight());
            profile.setAddress(address);
            profile.setLocation(address != null ? address.getAddressStreet() : null);
            user.setUserProfile(profile);
            userRepository.save(user);

            return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.REGISTER_SUCCESS));

        } catch (Exception e) {
            log.error("❌ Đăng ký thất bại cho [{}]: {}", request.getUsername(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ApiResponseDTO<>(false, MessageConstants.REGISTER_FAILED));
        }
    }
    public ResponseEntity<ApiResponseDTO<?>> refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(false, "Refresh token không hợp lệ hoặc đã hết hạn"));
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        String accessToken = tokenProvider.createToken(user.getUsername(), user.getUserId());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Refresh thành công", tokens));
    }

    public String sendResetPasswordOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống."));

        String otp = redisOtpService.generateOtp(email);

        String displayName = (user.getUserProfile() != null && user.getUserProfile().getFullName() != null)
                ? user.getUserProfile().getFullName()
                : user.getUsername();

        // ✅ Nội dung email
        String content = "<p>Xin chào <b>" + displayName + "</b>,</p>"
                + "<p>Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản của mình.</p>"
                + "<p>Mã OTP của bạn là: <b style='color:red; font-size: 18px'>" + otp + "</b></p>"
                + "<p>Mã OTP chỉ có hiệu lực trong <b>5 phút</b>. Vui lòng không chia sẻ mã này với bất kỳ ai.</p>"
                + "<br><p>Trân trọng,<br>Hệ thống Hỗ trợ Hiến máu</p>";

        // ✅ Gửi email
        emailService.sendEmail(user, "Mã OTP đặt lại mật khẩu", content, EmailType.RESET_PASSWORD.name());

        // ✅ Trả về OTP để hiển thị trong response khi test
        return otp;
    }

    public void resetPassword(String email, String otp, String newPassword) {
        if (!redisOtpService.validateOtp(email, otp)) {
            throw new RuntimeException("Mã OTP không hợp lệ hoặc đã hết hạn.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email này."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        String content = "<p>Bạn đã đổi mật khẩu thành công.</p>";
        emailService.sendEmail(user, "Đổi mật khẩu thành công", content, EmailType.SYSTEM.name());
    }

    public void changePassword(UserPrincipal principal, ChangePasswordDTO dto) {
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp.");
        }

        if (dto.getNewPassword().equals(dto.getCurrentPassword())) {
            throw new RuntimeException("Mật khẩu mới không được trùng mật khẩu hiện tại.");
        }

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng.");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        String content = "<p>Bạn vừa đổi mật khẩu thành công.</p>";
        emailService.sendEmail(user, "Đổi mật khẩu", content, EmailType.SYSTEM.name());
    }

    public ResponseEntity<ApiResponseDTO<?>> getCurrentUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(new ApiResponseDTO<>(false, "Người dùng chưa xác thực"));
        }

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        UserProfile profile = user.getUserProfile();
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().getName());
        dto.setEnable(user.isEnable());

        if (profile != null) {
            dto.setFullName(profile.getFullName());
            dto.setDob(profile.getDob());
            dto.setGender(profile.getGender());
            dto.setBloodType(profile.getBloodType());
            dto.setPhone(profile.getPhone());
            dto.setLandline(profile.getLandline());
            dto.setOccupation(profile.getOccupation());
            dto.setCitizenId(profile.getCitizenId());
            dto.setWeight(profile.getWeightKg());
            dto.setHeight(profile.getHeightCm());
            dto.setEmergencyContact(profile.getEmergencyContact());
            dto.setAltPhone(profile.getAltPhone());
            dto.setLastDonationDate(profile.getLastDonationDate());
            dto.setRecoveryTime(profile.getRecoveryTime());
            dto.setLocation(profile.getLocation());
            dto.setHasInsurance(profile.isHasInsurance());
            dto.setInsuranceCardNumber(profile.getInsuranceCardNumber());
            dto.setInsuranceValidTo(profile.getInsuranceValidTo());

            Address address = profile.getAddress();
            if (address != null) {
                dto.setAddressId(address.getAddressId());
                dto.setAddressFull(address.getAddressStreet());
                dto.setLatitude(address.getLatitude());
                dto.setLongitude(address.getLongitude());
            }
        }

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Thông tin người dùng hiện tại", dto));
    }

    private LoginResponseDTO buildLoginResponse(User user) {
        UserProfile profile = user.getUserProfile();
        String accessToken = tokenProvider.createToken(user.getUsername(), user.getUserId());
        String refreshToken = tokenProvider.createRefreshToken(user.getUsername(), user.getUserId());

        return new LoginResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getName(),
                user.isEnable(),
                accessToken,
                refreshToken,
                profile != null ? profile.getFullName() : null,
                profile != null ? profile.getPhone() : null,
                profile != null ? profile.getGender() : null,
                profile != null ? profile.getEmergencyContact() : null
        );
    }



}

