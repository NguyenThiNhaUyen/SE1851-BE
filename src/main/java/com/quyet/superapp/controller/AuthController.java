package com.quyet.superapp.controller;

import com.quyet.superapp.config.jwt.UserPrincipal;
import com.quyet.superapp.dto.*;
import com.quyet.superapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final UserService userService;


     @PostMapping("/login")

     public ResponseEntity<ApiResponseDTO<?>> Login(@RequestBody LoginRequestDTO loginRequest){
         return userService.login(loginRequest);
     }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDTO<?>> logout(@AuthenticationPrincipal UserPrincipal principal) {
        return userService.logout(principal);
    }

     @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<?>> register(@RequestBody RegisterRequestDTO registerRequest){
         return userService.register(registerRequest);
     }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<?>> getCurrentUser() {
        return userService.getCurrentUserProfile();
    }

    // ✅ Gửi mã OTP qua email
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponseDTO<?>> sendOtp(@RequestParam String email) {
        userService.sendResetPasswordOtp(email);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Mã OTP đã được gửi tới email"));
    }

    // ✅ Đặt lại mật khẩu bằng OTP
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDTO<?>> resetPassword(@RequestBody ResetPasswordDTO dto) {
        userService.resetPassword(dto.getEmail(), dto.getOtp(), dto.getNewPassword());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Đổi mật khẩu thành công"));
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDTO<?>> changePassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody @Valid ChangePasswordDTO dto
    ) {
        userService.changePassword(principal, dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Đổi mật khẩu thành công"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponseDTO<?>> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        return userService.refreshToken(refreshToken);
    }

}
