package com.quyet.superapp.service;

import com.quyet.superapp.config.jwt.JwtTokenProvider;
import com.quyet.superapp.dto.LoginRequestDTO;
import com.quyet.superapp.dto.LoginResponseDTO;
import com.quyet.superapp.dto.RegisterRequestDTO;
import com.quyet.superapp.entity.Role;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.repository.RoleRepository;
import com.quyet.superapp.repository.UserRepository;
import com.quyet.superapp.repository.address.AddressRepository;
import com.quyet.superapp.repository.address.WardRepository;
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
import com.quyet.superapp.entity.address.Ward;

import java.util.Optional;
import com.quyet.superapp.entity.address.Address;

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

    /**
     * Đăng ký user mới
     */
    public ResponseEntity<?> register(RegisterRequestDTO request) {
        // Kiểm tra username và email đã tồn tại chưa
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }

        // Tìm role
        Role role = roleRepository.findByName(
                Optional.ofNullable(request.getRole()).map(String::toUpperCase).orElse("MEMBER")
        ).orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));

        // Tạo user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnable(true);
        user.setRole(role);

        // ✅ Tìm Ward theo wardId từ AddressDTO
        Address address = null;
        if (request.getAddress() != null && request.getAddress().getWardId() != null) {
            Ward ward = wardRepository.findById(request.getAddress().getWardId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phường/xã phù hợp"));

            address = new Address();
            address.setAddressStreet(request.getAddress().getAddressStreet());
            address.setWard(ward);
            addressRepository.save(address);
        }

        // Tạo profile
        UserProfile profile = new UserProfile();
        profile.setFullName(request.getFirstName() + " " + request.getLastName());
        profile.setDob(request.getDob());
        profile.setGender(request.getGender());
        profile.setPhone(request.getPhone());
        profile.setLocation(null);
        profile.setUser(user);
        profile.setAddress(address);

        // Gán profile cho user và lưu
        user.setUserProfile(profile);
        userRepository.save(user);

        return ResponseEntity.ok("Đăng ký thành công");
    }


}
