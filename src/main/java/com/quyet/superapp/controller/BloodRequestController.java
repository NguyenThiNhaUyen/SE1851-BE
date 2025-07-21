package com.quyet.superapp.controller;

import com.quyet.superapp.dto.ApproveBloodRequestDTO;
import com.quyet.superapp.dto.BloodRequestDTO;
import com.quyet.superapp.dto.CreateBloodRequestDTO;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.mapper.BloodRequestMapper;
import com.quyet.superapp.service.BloodRequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/blood-requests")
@RequiredArgsConstructor

@CrossOrigin(origins = "http://localhost:5173")
public class BloodRequestController {

    private final BloodRequestService requestService;
    private final BloodInventoryService inventoryService;
    private final UserRepository userRepository;
    private final PatientService patientService;
    private final PaymentService paymentService;

    @GetMapping("/{id}/payment-info")
    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN', 'STAFF')")
    public ResponseEntity<PaymentInfoDTO> getPaymentInfo(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.getPaymentInfo(id));
    }


    @PostMapping("/{id}/pay")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> payForRequest(@PathVariable Long id) {
        paymentService.processPayment(id); // tính giá, giảm BHYT, lưu giao dịch
        return ResponseEntity.ok(Map.of("message", "✅ Thanh toán thành công"));
    }




    // ================================
    // [STAFF] Tạo yêu cầu máu với bệnh nhân mới
    // ================================
    @PostMapping("/new")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> createRequestWithNewPatient(@Valid @RequestBody BloodRequestWithNewPatientDTO dto) {
        patientService.validateInsurance(dto);  // chỉ gọi, không cần gán
        BloodRequest created = requestService.createRequestWithNewPatient(dto);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(created));
    }


    // ================================
    // [STAFF] Tạo yêu cầu máu với bệnh nhân đã có
    // ================================
    @PostMapping("/existing")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> createRequestWithExistingPatient(@Valid @RequestBody BloodRequestWithExistingPatientDTO dto) {
        BloodRequest created = requestService.createRequestWithExistingPatient(dto);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(created));
    }

    // ================================
    // [STAFF + ADMIN] Kiểm tra số CMND/CCCD bệnh nhân
    // ================================
    @GetMapping("/check-citizen/{citizenId}")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<?> checkCitizenInfo(@PathVariable String citizenId) {
        Optional<User> userOpt = userRepository.findByCitizenId(citizenId);
        if (userOpt.isPresent()) {
            var u = userOpt.get();
            var p = u.getUserProfile();
            Map<String, Object> result = new HashMap<>();
            result.put("userId", u.getUserId());
            result.put("fullName", p.getFullName());
            result.put("phone", p.getPhone());
            result.put("age", (p.getDob() != null) ? Period.between(p.getDob(), LocalDate.now()).getYears() : null);
            result.put("gender", p.getGender());
            result.put("bloodGroup", (p.getBloodType() != null) ? p.getBloodType().getDescription() : null);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(Collections.emptyMap());
    }

    // ================================
    // [STAFF] Xác nhận số lượng máu nhận được
    // ================================
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<BloodRequestDTO> confirmReceived(@PathVariable Long id, @RequestParam int confirmedVolumeMl) {
        BloodRequest updated = requestService.confirmReceivedVolume(id, confirmedVolumeMl);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(updated));
    }

    // ================================
    // [ADMIN] Duyệt hoặc từ chối yêu cầu máu
    // ================================
    @PutMapping("/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodRequestDTO> approveRequest(@RequestBody ApproveBloodRequestDTO dto) {

