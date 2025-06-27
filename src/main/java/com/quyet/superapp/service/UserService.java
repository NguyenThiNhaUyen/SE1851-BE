package com.quyet.superapp.service;

import com.quyet.superapp.config.jwt.JwtTokenProvider;
import com.quyet.superapp.constant.MessageConstants;
import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.LoginRequestDTO;
import com.quyet.superapp.dto.LoginResponseDTO;
import com.quyet.superapp.dto.RegisterRequestDTO;
import com.quyet.superapp.entity.Role;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.repository.RoleRepository;
import com.quyet.superapp.repository.UserProfileRepository;
import com.quyet.superapp.repository.UserRepository;
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

import java.util.Optional;

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

    /**
     * Đăng nhập
     */
    public ResponseEntity<ApiResponseDTO<?>> login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException(MessageConstants.USER_NOT_FOUND));

            String jwt = tokenProvider.createToken(user.getUsername(), user.getUserId());

            LoginResponseDTO loginResponse = new LoginResponseDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().getName(),
                    user.isEnable(),
                    jwt
            );
            return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.LOGIN_SUCCESS, loginResponse));

        } catch (AuthenticationException e) {
            log.error("Đăng nhập thất bại cho username [{}]: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, MessageConstants.LOGIN_FAILED));
        }
    }
    /**
     * Đăng ký tài khoản mới
     */
    @Transactional
    public ResponseEntity<ApiResponseDTO<?>> register(RegisterRequestDTO request) {
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

            // Tìm vai trò
            Role role = roleRepository.findByName(
                    Optional.ofNullable(request.getRole()).map(String::toUpperCase).orElse("MEMBER")
            ).orElseThrow(() -> new RuntimeException(MessageConstants.ROLE_NOT_FOUND));

            // Tạo địa chỉ nếu có
            Address address = null;
            if (request.getAddress() != null && request.getAddress().getWardId() != null) {
                Ward ward = wardRepository.findById(request.getAddress().getWardId())
                        .orElseThrow(() -> new RuntimeException(MessageConstants.WARD_NOT_FOUND));

                address = new Address();
                address.setWard(ward);
                address.setAddressStreet(request.getAddress().getAddressStreet());
                addressRepository.save(address);
            }

            // Tạo user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getContactInfo().getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEnable(true);
            user.setRole(role);

            // Tạo profile
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

            log.info("Đăng ký thành công cho username: {}", request.getUsername());
            return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.REGISTER_SUCCESS));

        } catch (Exception e) {
            log.error("Đăng ký thất bại cho username [{}]: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO<>(false, MessageConstants.REGISTER_FAILED));
        }
    }
}
