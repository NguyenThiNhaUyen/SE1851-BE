package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodUnitDTO;
import com.quyet.superapp.entity.BloodUnit;
import com.quyet.superapp.enums.BloodUnitStatus;
import com.quyet.superapp.mapper.BloodUnitMapper;
import com.quyet.superapp.service.BloodUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/blood-units")
@RequiredArgsConstructor
public class BloodUnitController {

    private final BloodUnitService service;


    // ✅ Lấy tất cả đơn vị máu
    @GetMapping
    public ResponseEntity<List<BloodUnitDTO>> getAll() {
        List<BloodUnitDTO> result = service.getAllUnits().stream()

                .map(BloodUnitMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy đơn vị máu theo ID (PathVariable)

    @GetMapping("/{id}")
    public ResponseEntity<BloodUnitDTO> getById(@PathVariable Long id) {
        BloodUnit unit = service.getById(id);
        return ResponseEntity.ok(BloodUnitMapper.toDTO(unit));
    }


    @PostMapping
    public ResponseEntity<BloodUnitDTO> create(@RequestBody BloodUnitDTO dto) {
        BloodUnit entity = BloodUnitMapper.fromDTO(dto);
        BloodUnit saved = service.save(entity, dto.getBloodTypeId(), dto.getComponentId(), dto.getBloodBagId());
        return ResponseEntity.ok(BloodUnitMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BloodUnitDTO> update(@PathVariable Long id, @RequestBody BloodUnitDTO dto) {
        BloodUnit existing = service.getById(id);
        BloodUnit updated = BloodUnitMapper.fromDTO(dto);
        updated.setBloodUnitId(id);
        BloodUnit saved = service.save(updated, dto.getBloodTypeId(), dto.getComponentId(), dto.getBloodBagId());
        return ResponseEntity.ok(BloodUnitMapper.toDTO(saved));
    }


    // ✅ Tạo mới đơn vị máu
    @PostMapping
    public ResponseEntity<BloodUnitDTO> create(@RequestBody BloodUnitDTO dto) {
        BloodUnit entity = BloodUnitMapper.fromDTO(dto);
        BloodUnit saved = service.saveUnit(entity, dto.getBloodTypeId(), dto.getComponentId(), dto.getBloodBagId());
        return ResponseEntity.ok(BloodUnitMapper.toDTO(saved));
    }

    // ✅ Cập nhật đơn vị máu
    @PutMapping("/{id}")
    public ResponseEntity<BloodUnitDTO> update(@PathVariable Long id, @RequestBody BloodUnitDTO dto) {
        service.getById(id); // xác minh tồn tại
        BloodUnit updated = BloodUnitMapper.fromDTO(dto);
        updated.setBloodUnitId(id);
        BloodUnit saved = service.saveUnit(updated, dto.getBloodTypeId(), dto.getComponentId(), dto.getBloodBagId());
        return ResponseEntity.ok(BloodUnitMapper.toDTO(saved));
    }

    // ✅ Xóa đơn vị máu

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/expiring")

    public ResponseEntity<List<BloodUnitDTO>> findExpiringBefore(
            @RequestParam("before") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BloodUnitDTO> result = service.findExpiringBefore(date).stream()
                .map(BloodUnitMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }


    // ✅ Tìm theo mã đơn vị
    @GetMapping("/filter/by-code")
    public ResponseEntity<BloodUnitDTO> findByUnitCode(@RequestParam String code) {
        BloodUnit unit = service.getByUnitCode(code);
>>>>>>> origin/main
        return ResponseEntity.ok(BloodUnitMapper.toDTO(unit));
    }
}
