package com.quyet.superapp.controller;

import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.LoginRequestDTO;
import com.quyet.superapp.dto.RegisterRequestDTO;
import com.quyet.superapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserService userService;

     @PostMapping("/login")
     public ResponseEntity<ApiResponseDTO<?>> Login(@RequestBody LoginRequestDTO loginRequest){
         return userService.login(loginRequest);
     }

     @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<?>> register(@RequestBody RegisterRequestDTO registerRequest){
         return userService.register(registerRequest);
     }

}
