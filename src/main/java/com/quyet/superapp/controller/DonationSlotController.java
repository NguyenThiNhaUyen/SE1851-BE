package com.quyet.superapp.controller;

import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.dto.DonationSlotDTO;
import com.quyet.superapp.dto.SlotLoadDTO;
import com.quyet.superapp.enums.SlotStatus;
import com.quyet.superapp.service.DonationRegistrationService;
import com.quyet.superapp.service.DonationSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class DonationSlotController {

    private final DonationSlotService donationSlotService;
    private final DonationRegistrationService donationRegistrationService;

    //Tạo mới slot hiến máu (ADMIN/STAFF)
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponseDTO<DonationSlotDTO>> createSlot(@RequestBody DonationSlotDTO dto) {
        DonationSlotDTO created = donationSlotService.create(dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Tạo slot thành công", created));
    }

    //Lấy tất cả slot
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<DonationSlotDTO>>> getAllSlots() {
        List<DonationSlotDTO> slots = donationSlotService.getAll();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Lấy danh sách slot thành công", slots));
    }

    //Lấy slot theo ID
    @GetMapping("/by-id")
    public ResponseEntity<ApiResponseDTO<DonationSlotDTO>> getSlotById(@RequestParam Long id) {
        DonationSlotDTO dto = donationSlotService.getById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Lấy slot thành công", dto));
    }

    //Lấy slot theo ngày cụ thể
    @GetMapping("/by-date")
    public ResponseEntity<ApiResponseDTO<List<DonationSlotDTO>>> getByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<DonationSlotDTO> slots = donationSlotService.getSlotsByDate(date);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Lấy slot theo ngày thành công", slots));
    }

    //Lấy slot theo trạng thái
    @GetMapping("/by-status")
    public ResponseEntity<ApiResponseDTO<List<DonationSlotDTO>>> getByStatus(
            @RequestParam("status") SlotStatus status
    ) {
        List<DonationSlotDTO> slots = donationSlotService.getSlotsByStatus(status);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Lấy slot theo trạng thái thành công", slots));
    }

    //Gợi ý slot hôm nay còn trống
    @GetMapping("/available-today")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ApiResponseDTO<List<DonationSlotDTO>>> getAvailableToday() {
        List<DonationSlotDTO> slots = donationSlotService.getTodayAvailableSlots();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Lấy slot còn trống hôm nay thành công", slots));
    }

    //Gợi ý slot còn trống theo số lượng yêu cầu
    @GetMapping("/suggest")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ApiResponseDTO<List<DonationSlotDTO>>> suggestSlots(
            @RequestParam("requiredCapacity") int requiredCapacity
    ) {
        List<DonationSlotDTO> suggestions = donationSlotService.getSuggestedSlots(requiredCapacity);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Gợi ý slot thành công", suggestions));
    }

    //kiểm tra 1 slot còn chỗ không
    @GetMapping("/check-availability")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponseDTO<Boolean>> isSlotAvailable(@RequestParam Long slotId) {
        boolean available = donationSlotService.isSlotAvailable(slotId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Kiểm tra thành công", available));
    }

    @GetMapping("/load")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponseDTO<List<SlotLoadDTO>>> getSlotLoad() {
        List<SlotLoadDTO> stats = donationSlotService.getSlotLoadStats();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Phân tích tải slot thành công", stats));
    }
    @GetMapping("/registrations")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponseDTO<List<DonationRegistrationDTO>>> getRegistrationsBySlot(
            @RequestParam("slotId") Long slotId
    ) {
        List<DonationRegistrationDTO> registrations = donationRegistrationService.getBySlotId(slotId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Lấy danh sách đăng ký theo slot thành công", registrations));
    }

    // Gợi ý slot tốt nhất
    @GetMapping("/best-slot")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ApiResponseDTO<DonationSlotDTO>> getBestSlot() {
        DonationSlotDTO dto = donationSlotService.getBestAvailableSlot();

        if (dto == null) {
            return ResponseEntity.ok(new ApiResponseDTO<>(false, "Không có slot phù hợp", null));
        }

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Gợi ý slot tốt nhất thành công", dto));
    }

}
