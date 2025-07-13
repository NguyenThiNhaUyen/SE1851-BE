//package com.quyet.superapp.controller;
//
//import com.quyet.superapp.config.jwt.UserPrincipal;
//import com.quyet.superapp.dto.DonorReadinessRequestDTO;
//import com.quyet.superapp.service.DonorReadinessService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/donors")
//@RequiredArgsConstructor
//public class DonorReadinessController {
//
//    private final DonorReadinessService donorReadinessService;
//
//    @PostMapping("/readiness")
//    public ResponseEntity<?> setReadiness(@RequestBody DonorReadinessRequestDTO dto,
//                                          @AuthenticationPrincipal UserPrincipal principal) {
//        Long userId = principal.getUserId(); // ✔ Gọi đúng phương thức đã khai báo
//
//        // Trong controller
//        String message = donorReadinessService.processReadiness(userId, dto.getReadinessLevel());
//
//        return ResponseEntity.ok().body(Map.of("message", message));
//    }
//}
