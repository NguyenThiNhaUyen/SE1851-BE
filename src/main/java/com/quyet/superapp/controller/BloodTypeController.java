package com.quyet.superapp.controller;


import com.quyet.superapp.dto.BloodTypeDTO;
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

    @GetMapping
    public List<BloodTypeDTO> getAllBloodTypes() {
        return bloodTypeService.getAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // chỉ admin được thêm
    public ResponseEntity<BloodTypeDTO> createBloodType(@RequestBody BloodTypeDTO dto) {
        BloodTypeDTO created = bloodTypeService.create(dto);
        return ResponseEntity.ok(created);
    }

}
