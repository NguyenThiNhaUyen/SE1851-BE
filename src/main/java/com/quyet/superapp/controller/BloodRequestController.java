package com.quyet.superapp.controller;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.mapper.BloodRequestMapper;
import com.quyet.superapp.repository.UserRepository;
import com.quyet.superapp.service.BloodInventoryService;
import com.quyet.superapp.service.BloodRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/blood-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BloodRequestController {

    private final BloodRequestService requestService;
    private final BloodInventoryService bloodInventoryService;
    private final UserRepository userRepository;

    @GetMapping("/admin/requests/completed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getCompletedRequests() {
        return ResponseEntity.ok(requestService.getCompletedRequests());
    }


    @GetMapping("/admin/requests/processing")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getProcessingRequests() {
        return ResponseEntity.ok(requestService.getProcessingRequests());
    }



    @GetMapping("/pricing/{componentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Integer> getComponentPrice(@PathVariable Long componentId) {
        int unitPrice = requestService.getPriceForComponent(componentId);
        return ResponseEntity.ok(unitPrice);
    }


    @GetMapping("/check-citizen/{citizenId}")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<?> checkCitizenId(@PathVariable String citizenId) {
        Optional<User> userOpt = userRepository.findByCitizenId(citizenId);
        if (userOpt.isPresent()) {
            var u = userOpt.get();
            var p = u.getUserProfile();
            return ResponseEntity.ok(Map.of(
                    "userId", u.getUserId(),
                    "fullName", p.getFullName(),
                    "phone", p.getPhone(),
                    "age", p.getDob() != null ? Period.between(p.getDob(), LocalDate.now()).getYears() : null,
                    "gender", p.getGender(),
                    "bloodGroup", p.getBloodType() != null ? p.getBloodType().getDescription() : null
            ));
        }
        return ResponseEntity.ok(Map.of());
    }


    // ✅ [STAFF] Gửi yêu cầu máu mới
    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<BloodRequestDTO> createBloodRequest(@RequestBody CreateBloodRequestDTO dto) {
        BloodRequest created = requestService.createRequest(dto);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(created));
    }

    // ✅ [ADMIN] Duyệt hoặc từ chối yêu cầu máu
    @PutMapping("/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodRequestDTO> approveBloodRequest(@RequestBody ApproveBloodRequestDTO dto) {
        BloodRequest approved = requestService.approveRequest(dto);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(approved));
    }

    // ✅ [ADMIN or STAFF] Lấy chi tiết yêu cầu máu theo ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BloodRequestDTO> getRequestById(@PathVariable("id") Long id) {
        BloodRequestDTO dto = requestService.getById(id);
        return ResponseEntity.ok(dto);
    }

    // ✅ [ADMIN] Lấy danh sách yêu cầu đã hoàn tất
//    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<BloodRequestDTO>> getCompletedRequests() {
//        return ResponseEntity.ok(requestService.getCompletedRequests());
//    }

    // ✅ [ADMIN] Lấy danh sách yêu cầu đang xử lý
//    @GetMapping("/admin/active")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<BloodRequestDTO>> getActiveRequests() {
//        return ResponseEntity.ok(requestService.getActiveRequests());
//    }

    // ✅ [ADMIN] Cập nhật nhanh trạng thái
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodRequestDTO> updateStatus(@PathVariable("id") Long id, @RequestParam String status) {
        BloodRequest updated = requestService.updateStatus(id, status);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(updated));
    }

    // ✅ [ADMIN or STAFF] Lọc yêu cầu theo trạng thái & độ khẩn
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<BloodRequestDTO>> getByFilter(
            @RequestParam(required = false) String urgency,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(requestService.filterRequests(urgency, status));
    }

    // ✅ [ADMIN] Lấy yêu cầu KHẨN CẤP đang chờ duyệt
    @GetMapping("/admin/urgent/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getUrgentPendingRequests() {
        return ResponseEntity.ok(requestService.getUrgentPendingRequests());
    }

    // ✅ [ADMIN] Lịch sử yêu cầu KHẨN CẤP
    @GetMapping("/admin/urgent/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getUrgentRequestHistory() {
        return ResponseEntity.ok(requestService.getUrgentRequestHistory());
    }

    // ✅ [ADMIN] Yêu cầu KHẨN CẤP đang hoạt động
    @GetMapping("/admin/urgent/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getUrgentActiveRequests() {
        return ResponseEntity.ok(requestService.getUrgentActiveRequests());
    }



    // ✅ [STAFF] Xác nhận số lượng máu đã nhận
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<BloodRequestDTO> confirmReceivedBlood(
            @PathVariable("id") Long id,
            @RequestParam int confirmedVolumeMl
    ) {
        BloodRequest request = requestService.confirmReceivedVolume(id, confirmedVolumeMl);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(request));
    }

    // ✅ [ADMIN or STAFF] Kiểm tra tồn kho máu theo yêu cầu
    @GetMapping("/{id}/check-inventory")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<InventoryCheckResultDTO> checkInventory(@PathVariable("id") Long id) {
        BloodRequest request = requestService.findById(id);
        InventoryCheckResultDTO result = bloodInventoryService.checkInventoryForRequest(request);
        return ResponseEntity.ok(result);
    }
}
