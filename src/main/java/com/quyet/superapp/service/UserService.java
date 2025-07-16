package com.quyet.superapp.service;

import com.quyet.superapp.config.jwt.JwtTokenProvider;
import com.quyet.superapp.constant.MessageConstants;
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

    /**
     * Đăng nhập và trả về LoginResponseDTO gồm JWT
     */
    public ResponseEntity<?> login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

            // Tạo token JWT
            String jwt = tokenProvider.createToken(user.getUsername(), user.getUserId());

            // Build response DTO
            LoginResponseDTO loginResponse = new LoginResponseDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().getName(),
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



    /**
     * Đăng ký tài khoản mới
     */
    @Transactional
    public ResponseEntity<?> register(RegisterRequestDTO request) {
        try {
            // Kiểm tra trùng thông tin
            if (userRepository.existsByUsername(request.getUsername())) {
                log.warn("Tên đăng nhập đã tồn tại: {}", request.getUsername());
                return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, MessageConstants.USERNAME_EXISTS));
            }
            if (userRepository.existsByEmail(request.getContactInfo().getEmail())) {
                log.warn("Email đã tồn tại (User): {}", request.getContactInfo().getEmail());
                return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, MessageConstants.EMAIL_EXISTS));
            }
            if (userProfileRepository.existsByCitizenId(request.getCccd())) {
                log.warn("CCCD đã tồn tại: {}", request.getCccd());
                return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, MessageConstants.CCCD_EXISTS));
            }
            if (userProfileRepository.existsByEmail(request.getContactInfo().getEmail())) {
                log.warn("Email đã tồn tại (Profile): {}", request.getContactInfo().getEmail());
                return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, MessageConstants.EMAIL_PROFILE_EXISTS));
            }

            Role role = roleRepository.findByName(
                    String.valueOf(RoleEnum.valueOf(Optional.ofNullable(request.getRole()).map(String::toUpperCase).orElse("MEMBER")))
            ).orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));

            Address address = null;
            if (request.getAddress() != null && request.getAddress().getWardId() != null) {
                Ward ward = wardRepository.findById(request.getAddress().getWardId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy phường/xã phù hợp"));

                address = new Address();
                address.setAddressStreet(request.getAddress().getAddressStreet());
                address.setWard(ward);
                addressRepository.save(address);
            }

            // Tạo user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getContactInfo().getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEnable(true);
            user.setRole(role);

            UserProfile profile = new UserProfile();
            profile.setUser(user);
            profile.setFullName(request.getFullName());
            profile.setDob(request.getDob());
            profile.setGender(request.getGender());
            profile.setPhone(request.getContactInfo().getPhone());
            profile.setEmail(request.getContactInfo().getEmail());
            profile.setCitizenId(request.getCccd());
            profile.setOccupation(request.getOccupation());
            profile.setWeight(request.getWeight());
            profile.setHeight(request.getHeight());
            profile.setAddress(address);
            profile.setLocation(address != null ? address.getAddressStreet() : null);

            user.setUserProfile(profile);
            userRepository.save(user);

            return ResponseEntity.ok("Đăng ký thành công");

        } catch (Exception e) {
            log.error("Đăng ký thất bại cho username [{}]: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.internalServerError().body("Đăng ký thất bại");
        }
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
        Role role = roleRepository.findByName(String.valueOf(roleEnum))
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
}
