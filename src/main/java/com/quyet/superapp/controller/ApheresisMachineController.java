package com.quyet.superapp.controller;

import com.quyet.superapp.dto.ApheresisMachineDTO;
import com.quyet.superapp.service.ApheresisMachineService;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
=======
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
>>>>>>> origin/main
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/apheresis-machines")
<<<<<<< HEAD
=======
@Validated
>>>>>>> origin/main
public class ApheresisMachineController {

    private final ApheresisMachineService apheresisMachineService;

    // ✅ Lấy danh sách tất cả máy
    @GetMapping
    public ResponseEntity<List<ApheresisMachineDTO>> getAll() {
        return ResponseEntity.ok(apheresisMachineService.getAll());
    }

    // ✅ Lấy chi tiết máy theo ID
<<<<<<< HEAD
    @GetMapping("/{id}")
    public ResponseEntity<ApheresisMachineDTO> getById(@PathVariable Long id) {
=======
    @GetMapping("/by-id")
    public ResponseEntity<ApheresisMachineDTO> getById(@RequestParam Long id) {
>>>>>>> origin/main
        return apheresisMachineService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Tạo máy mới
    @PostMapping
<<<<<<< HEAD
    public ResponseEntity<ApheresisMachineDTO> create(@RequestBody ApheresisMachineDTO dto) {
=======
    public ResponseEntity<ApheresisMachineDTO> create( @RequestBody ApheresisMachineDTO dto) {
>>>>>>> origin/main
        return ResponseEntity.ok(apheresisMachineService.create(dto));
    }

    // ✅ Cập nhật máy
<<<<<<< HEAD
    @PutMapping("/{id}")
    public ResponseEntity<ApheresisMachineDTO> update(@PathVariable Long id,
=======
    @PutMapping
    public ResponseEntity<ApheresisMachineDTO> update(@RequestParam  Long id,
>>>>>>> origin/main
                                                      @RequestBody ApheresisMachineDTO dto) {
        return ResponseEntity.ok(apheresisMachineService.update(id, dto));
    }

    // ✅ Xoá máy theo ID
<<<<<<< HEAD
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
=======
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam  Long id) {
>>>>>>> origin/main
        apheresisMachineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Lấy danh sách máy đang hoạt động
    @GetMapping("/active")
    public ResponseEntity<List<ApheresisMachineDTO>> getActiveMachines() {
        return ResponseEntity.ok(apheresisMachineService.getActiveMachines());
    }

    // ✅ Lọc máy cần bảo trì trước ngày
    @GetMapping("/due-maintenance")
    public ResponseEntity<List<ApheresisMachineDTO>> getMachinesDueForMaintenance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate
    ) {
        return ResponseEntity.ok(apheresisMachineService.getMachinesDueForMaintenance(beforeDate));
    }

}
