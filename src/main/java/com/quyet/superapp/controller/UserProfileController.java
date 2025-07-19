package com.quyet.superapp.controller;

<<<<<<< HEAD
import com.quyet.superapp.dto.UserProfileDTO;
=======
import com.quyet.superapp.dto.UserProfileCreateDTO;
import com.quyet.superapp.dto.UserProfileUpdateDTO;
import com.quyet.superapp.dto.UserProfileResponseDTO;
>>>>>>> origin/main
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.mapper.UserProfileMapper;
import com.quyet.superapp.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
<<<<<<< HEAD
=======
import org.springframework.validation.annotation.Validated;
>>>>>>> origin/main
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
<<<<<<< HEAD
@RequestMapping("/api/userprofiles")
@RequiredArgsConstructor
=======
@RequestMapping("/api/userprofile")
@RequiredArgsConstructor
@Validated
>>>>>>> origin/main
public class UserProfileController {

    private final UserProfileService userProfileService;

    // ✅ [GET] Lấy danh sách tất cả hồ sơ người dùng
    @GetMapping
<<<<<<< HEAD
    public ResponseEntity<List<UserProfileDTO>> getAllProfiles() {
        List<UserProfileDTO> profiles = userProfileService.getAllProfiles()
                .stream()
                .map(UserProfileMapper::toDTO)
=======
    public ResponseEntity<List<UserProfileResponseDTO>> getAllProfiles() {
        List<UserProfileResponseDTO> profiles = userProfileService.getAllProfiles()
                .stream()
                .map(UserProfileMapper::toResponseDTO)
>>>>>>> origin/main
                .collect(Collectors.toList());
        return ResponseEntity.ok(profiles);
    }

    // ✅ [GET] Lấy thông tin hồ sơ theo userId
<<<<<<< HEAD
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getProfileByUserId(@PathVariable Long userId) {
        UserProfile profile = userProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(UserProfileMapper.toDTO(profile));
    }

    // ✅ [POST] Tạo mới hồ sơ người dùng
    @PostMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> createProfile(@PathVariable Long userId,
                                                        @RequestBody @Valid UserProfileDTO dto) {
        UserProfile createdProfile = userProfileService.createProfile(userId, dto);
        return ResponseEntity.ok(UserProfileMapper.toDTO(createdProfile));
    }

    // ✅ [PUT] Cập nhật hồ sơ người dùng
    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> updateProfile(@PathVariable Long userId,
                                                        @RequestBody @Valid UserProfileDTO dto) {
        UserProfile updatedProfile = userProfileService.updateProfile(userId, dto);
        return ResponseEntity.ok(UserProfileMapper.toDTO(updatedProfile));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userProfileService.deleteById(id);
    }

    /**
     * Lấy hồ sơ người dùng hiện tại (dựa vào JWT -> lấy username)
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('MEMBER', 'STAFF', 'ADMIN')")
    public ResponseEntity<UserProfileDTO> getMyProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfile profile = userProfileService.getByUsername(username);
        UserProfileDTO dto = UserProfileMapper.toDTO(profile);  // ✅ dùng mapper để format lại
        return ResponseEntity.ok(dto);                          // ✅ trả về DTO mới
    }



=======
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
>>>>>>> origin/main
}
