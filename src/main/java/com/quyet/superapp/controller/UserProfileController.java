package com.quyet.superapp.controller;

import com.quyet.superapp.dto.UserProfileCreateDTO;
import com.quyet.superapp.dto.UserProfileUpdateDTO;
import com.quyet.superapp.dto.UserProfileResponseDTO;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.mapper.UserProfileMapper;
import com.quyet.superapp.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/userprofile")
@RequiredArgsConstructor
@Validated
public class UserProfileController {

    private final UserProfileService userProfileService;

    // ✅ [GET] Lấy danh sách tất cả hồ sơ người dùng
    @GetMapping
    public ResponseEntity<List<UserProfileResponseDTO>> getAllProfiles() {
        List<UserProfileResponseDTO> profiles = userProfileService.getAllProfiles()
                .stream()
                .map(UserProfileMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(profiles);
    }

    // ✅ [GET] Lấy thông tin hồ sơ theo userId
    @GetMapping("/user-id")
    public ResponseEntity<UserProfileResponseDTO> getProfileByUserId(@RequestParam Long userId) {
        UserProfile profile = userProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(UserProfileMapper.toResponseDTO(profile));
    }

    // ✅ [POST] Tạo mới hồ sơ người dùng
    @PostMapping
    public ResponseEntity<UserProfileResponseDTO> createProfile(
            @RequestParam Long userId,
            @RequestBody @Valid UserProfileCreateDTO dto) {
        UserProfile created = userProfileService.createProfile(userId, dto);
        return ResponseEntity.ok(UserProfileMapper.toResponseDTO(created));
    }

    // ✅ [PUT] Cập nhật hồ sơ người dùng
    @PutMapping
    public ResponseEntity<UserProfileResponseDTO> updateProfile(
            @RequestParam Long userId,
            @RequestBody @Valid UserProfileUpdateDTO dto) {
        UserProfile updated = userProfileService.updateProfile(userId, dto);
        return ResponseEntity.ok(UserProfileMapper.toResponseDTO(updated));
    }

    // ✅ [DELETE] Xóa hồ sơ theo ID
    @DeleteMapping("/by-id")
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        userProfileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ [GET] Lấy hồ sơ của người đang đăng nhập
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('MEMBER', 'STAFF', 'ADMIN')")
    public ResponseEntity<UserProfileResponseDTO> getMyProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfile profile = userProfileService.getByUsername(username);
        return ResponseEntity.ok(UserProfileMapper.toResponseDTO(profile));
    }
}
