package com.quyet.superapp.service;

import com.quyet.superapp.dto.LoginRequestDTO;
import com.quyet.superapp.dto.LoginResponseDTO;
import com.quyet.superapp.dto.RegisterRequestDTO;
import com.quyet.superapp.entity.Role;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.repository.RoleRepository;
import com.quyet.superapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;

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
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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


            LoginResponseDTO loginResponse = new LoginResponseDTO(

                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().getName(),
                    user.isEnable()
            );
            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Tài khoản hoặc mật khẩu không đúng");
        }
    }


    public ResponseEntity<?> register(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username đã tồn tại");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }

        Role roleMember = roleRepository.findByName("MEMBER")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role MEMBER trong hệ thống"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnable(true);
        String roleName = Optional.ofNullable(request.getRole())
                .map(String::toUpperCase)
                .orElse("MEMBER");

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vai trò: " + roleName));

        user.setRole(role);

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setFullName(request.getFirstName() + " " + request.getLastName());
        profile.setDob(request.getDob());
        profile.setGender(request.getGender());
        profile.setPhone(request.getPhone());
        profile.setCitizenId(request.getCccd());
        profile.setAddress(request.getAddress());

        // Gán profile vào user
        user.setUserProfile(profile);


        userRepository.save(user);
        return ResponseEntity.ok("Đăng ký thành công");
    }

}
