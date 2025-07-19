package com.quyet.superapp.controller;

<<<<<<< HEAD
import com.quyet.superapp.dto.BloodInventoryAlertDTO;
import com.quyet.superapp.dto.BloodInventoryDTO;
import com.quyet.superapp.entity.BloodComponent;
=======
import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.dto.BloodInventoryDTO;
>>>>>>> origin/main
import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.service.BloodService;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
=======
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
>>>>>>> origin/main
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-inventory")
@RequiredArgsConstructor
public class BloodInventoryController {

    private final BloodService bloodService;

    @GetMapping
<<<<<<< HEAD
    public List<BloodInventoryDTO> getAll() {
        return bloodService.getAllInventoryDTO();
    }

    @GetMapping("/status/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<BloodInventoryAlertDTO>> getAllInventoryStatus() {
        return ResponseEntity.ok(bloodService.getAllInventoryStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodInventory> getById(@PathVariable Long id) {
        return bloodService.getInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public BloodInventory add(@RequestBody BloodInventory inventory) {
        return bloodService.addBlood(inventory);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BloodInventory> update(@PathVariable Long id, @RequestBody BloodInventory inventory) {
        BloodInventory updated = bloodService.updateBlood(id, inventory);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bloodService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<BloodInventory> searchByTypeAndComponent(
            @RequestParam Long bloodTypeId,
            @RequestParam Long componentId
    ) {
        BloodType type = new BloodType();
        type.setBloodTypeId(bloodTypeId);
=======
    public ResponseEntity<ApiResponseDTO<List<BloodInventoryDTO>>> getAll() {
        List<BloodInventoryDTO> list = bloodService.getAllInventoryDTO();
        return ResponseEntity.ok(ApiResponseDTO.success("Lấy danh sách kho máu thành công", list));
    }

    @GetMapping("/by-id")
    public ResponseEntity<ApiResponseDTO<BloodInventory>> getById(@RequestParam Long id) {
        return bloodService.getInventoryById(id)
                .map(inv -> ResponseEntity.ok(ApiResponseDTO.success("Lấy thông tin kho máu theo ID", inv)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseDTO.fail("Không tìm thấy kho máu với ID đã cho")));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponseDTO<BloodInventory>> add(@RequestBody BloodInventory inventory) {
        BloodInventory saved = bloodService.addBlood(inventory);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Thêm kho máu thành công", saved));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponseDTO<BloodInventory>> update(@RequestParam Long id, @RequestBody BloodInventory inventory) {
        BloodInventory updated = bloodService.updateBlood(id, inventory);
        if (updated != null) {
            return ResponseEntity.ok(ApiResponseDTO.success("Cập nhật kho máu thành công", updated));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.fail("Không tìm thấy kho máu cần cập nhật"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@RequestParam Long id) {
        bloodService.deleteInventory(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Xoá kho máu thành công", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<BloodInventory>> searchByTypeAndComponent(
            @RequestParam Long bloodTypeId,
            @RequestParam Long componentId) {

        BloodType type = new BloodType();
        type.setBloodTypeId(bloodTypeId);

>>>>>>> origin/main
        BloodComponent component = new BloodComponent();
        component.setBloodComponentId(componentId);

        return bloodService.searchBloodByTypeAndComponent(type, component)
<<<<<<< HEAD
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/store/{bloodUnitId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> storeBlood(@PathVariable Long bloodUnitId) {
        bloodService.storeBloodUnit(bloodUnitId);
        return ResponseEntity.ok().build();
=======
                .map(inv -> ResponseEntity.ok(ApiResponseDTO.success("Tìm thấy kho máu theo nhóm máu và thành phần", inv)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseDTO.fail("Không tìm thấy kho máu theo yêu cầu")));
    }

    @PostMapping("/store")
    public ResponseEntity<ApiResponseDTO<Void>> storeBlood(@RequestParam Long bloodUnitId) {
        bloodService.storeBloodUnit(bloodUnitId);
        return ResponseEntity.ok(ApiResponseDTO.success("Lưu đơn vị máu vào kho thành công", null));
>>>>>>> origin/main
    }
}
