package com.quyet.superapp.controller;


import com.quyet.superapp.dto.BloodTypeDTO;
<<<<<<< HEAD
import com.quyet.superapp.dto.BloodTypeFullDTO;
=======
>>>>>>> origin/main
import com.quyet.superapp.service.BloodTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BloodTypeController {

    private final BloodTypeService bloodTypeService;

<<<<<<< HEAD
    /* ===== CRUD cơ bản ===== */
=======
>>>>>>> origin/main
    @GetMapping
    public List<BloodTypeDTO> getAllBloodTypes() {
        return bloodTypeService.getAll();
    }

    @PostMapping
<<<<<<< HEAD
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodTypeDTO> create(@RequestBody BloodTypeDTO dto) {
        return ResponseEntity.ok(bloodTypeService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodTypeDTO> update(@PathVariable Long id,
                                               @RequestBody BloodTypeDTO dto) {
        return ResponseEntity.ok(bloodTypeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bloodTypeService.delete(id);
        return ResponseEntity.ok().build();
    }

    /* ===== Danh sách chi tiết ===== */
    @GetMapping("/full")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<BloodTypeFullDTO>> getFullList() {
        return ResponseEntity.ok(bloodTypeService.getAllFull());
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        bloodTypeService.deactivate(id);
        return ResponseEntity.ok("Đã vô hiệu hóa nhóm máu");
    }

    @PutMapping("/{id}/reactivate")
    public ResponseEntity<?> reactivate(@PathVariable Long id) {
        bloodTypeService.reactivate(id);
        return ResponseEntity.ok("Đã khôi phục nhóm máu");
    }

}


=======
    @PreAuthorize("hasRole('ADMIN')") // chỉ admin được thêm
    public ResponseEntity<BloodTypeDTO> createBloodType(@RequestBody BloodTypeDTO dto) {
        BloodTypeDTO created = bloodTypeService.create(dto);
        return ResponseEntity.ok(created);
    }

}
>>>>>>> origin/main
