package com.quyet.superapp.controller;

import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.dto.BloodInventoryDTO;
import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.service.BloodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-inventory")
@RequiredArgsConstructor
public class BloodInventoryController {

    private final BloodService bloodService;

    @GetMapping
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

        BloodComponent component = new BloodComponent();
        component.setBloodComponentId(componentId);

        return bloodService.searchBloodByTypeAndComponent(type, component)
                .map(inv -> ResponseEntity.ok(ApiResponseDTO.success("Tìm thấy kho máu theo nhóm máu và thành phần", inv)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseDTO.fail("Không tìm thấy kho máu theo yêu cầu")));
    }

    @PostMapping("/store")
    public ResponseEntity<ApiResponseDTO<Void>> storeBlood(@RequestParam Long bloodUnitId) {
        bloodService.storeBloodUnit(bloodUnitId);
        return ResponseEntity.ok(ApiResponseDTO.success("Lưu đơn vị máu vào kho thành công", null));
    }
}
