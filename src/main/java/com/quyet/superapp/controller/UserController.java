package com.quyet.superapp.controller;

import com.quyet.superapp.dto.UserDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.mapper.UserMapper;
import com.quyet.superapp.repository.UserRepository;
import com.quyet.superapp.service.UserService;
import com.quyet.superapp.service.UserService1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


}
