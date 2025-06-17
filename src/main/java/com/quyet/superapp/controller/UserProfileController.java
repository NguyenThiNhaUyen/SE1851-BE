package com.quyet.superapp.controller;

import com.quyet.superapp.dto.UserProfileDTO;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.mapper.UserProfileMapper;
import com.quyet.superapp.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/userprofiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public List<UserProfile> getAll() {
        return userProfileService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getById(@PathVariable Long id) {
        return userProfileService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public UserProfile create(@RequestBody UserProfile obj) {
        return userProfileService.save(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> update(@PathVariable Long id, @RequestBody UserProfile obj) {
        Optional<UserProfile> existing = userProfileService.getById(id);
        return existing.isPresent()
                ? ResponseEntity.ok(userProfileService.save(obj))
                : ResponseEntity.notFound().build();
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
}
