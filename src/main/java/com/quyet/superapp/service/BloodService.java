package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodInventoryDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.BloodUnitStatus;
import com.quyet.superapp.mapper.BloodInventoryMapper;
import com.quyet.superapp.repository.BloodInventoryRepository;
import com.quyet.superapp.repository.BloodUnitRepository;
import com.quyet.superapp.repository.LabTestResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BloodService {

    private final BloodInventoryRepository inventoryRepo;
    private final BloodUnitRepository unitRepo;
    private final LabTestResultRepository labRepo;

    // ✅ Lấy toàn bộ kho máu (Entity)
    public List<BloodInventory> getInventory() {
        return inventoryRepo.findAll();
    }

    // ✅ Lấy kho máu theo ID
    public Optional<BloodInventory> getInventoryById(Long id) {
        return inventoryRepo.findById(id);
    }

    // ✅ Thêm mới 1 bản ghi kho máu (thủ công)
    public BloodInventory addBlood(BloodInventory inventory) {
        inventory.setLastUpdated(LocalDateTime.now());
        return inventoryRepo.save(inventory);
    }

    // ✅ Cập nhật bản ghi kho máu (thủ công)
    public BloodInventory updateBlood(Long id, BloodInventory updated) {
        return inventoryRepo.findById(id).map(inventory -> {
            inventory.setBloodType(updated.getBloodType());
            inventory.setComponent(updated.getComponent());
            inventory.setTotalQuantityMl(updated.getTotalQuantityMl());
            inventory.setLastUpdated(LocalDateTime.now());
            return inventoryRepo.save(inventory);
        }).orElse(null);
    }

    // ✅ Xóa bản ghi kho máu
    public void deleteInventory(Long id) {
        inventoryRepo.deleteById(id);
    }

    // ✅ Tìm kho máu theo nhóm máu và thành phần
    public Optional<BloodInventory> searchBloodByTypeAndComponent(BloodType bloodType, BloodComponent component) {
        return inventoryRepo.findByBloodTypeAndComponent(bloodType, component);
    }

    // ✅ Lấy danh sách DTO để hiển thị
    public List<BloodInventoryDTO> getAllInventoryDTO() {
        return inventoryRepo.findAll()
                .stream()
                .map(BloodInventoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Tự động lưu đơn vị máu vào kho sau khi xét nghiệm đạt
     * 1. Kiểm tra đơn vị máu và kết quả xét nghiệm
     * 2. Cập nhật trạng thái đơn vị máu
     * 3. Cộng thể tích vào kho máu tương ứng
     */
    @Transactional
    public void storeBloodUnit(Long bloodUnitId) {
        // B1. Tìm đơn vị máu
        BloodUnit unit = unitRepo.findById(bloodUnitId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Không tìm thấy đơn vị máu"));

        // B2. Kiểm tra kết quả xét nghiệm
        LabTestResult test = labRepo.findByBloodUnit_BloodUnitId(bloodUnitId)
                .orElseThrow(() -> new IllegalStateException("❌ Chưa có kết quả xét nghiệm"));

        if (!test.isPassed()) {
            throw new IllegalStateException("⚠️ Đơn vị máu không đạt yêu cầu xét nghiệm");
        }

        // B3. Cập nhật trạng thái đơn vị máu
        unit.setStatus(BloodUnitStatus.STORED);
        unit.setStoredAt(LocalDateTime.now());
        unitRepo.save(unit);

        // B4. Cập nhật vào kho máu
        updateInventory(unit);
    }

    // ✅ Hàm cập nhật kho máu từ đơn vị máu (reuse được nhiều nơi)
    private void updateInventory(BloodUnit unit) {
        BloodType type = unit.getBloodType();
        BloodComponent component = unit.getComponent();
        int volume = unit.getQuantityMl();

        BloodInventory inventory = inventoryRepo.findByBloodTypeAndComponent(type, component)
                .orElseGet(() -> {
                    BloodInventory newEntry = new BloodInventory();
                    newEntry.setBloodType(type);
                    newEntry.setComponent(component);
                    newEntry.setTotalQuantityMl(0);
                    newEntry.setCreatedAt(LocalDateTime.now());
                    return newEntry;
                });

        inventory.setTotalQuantityMl(inventory.getTotalQuantityMl() + volume);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepo.save(inventory);

        log.info("✅ Cập nhật kho: {} - {} + {}ml", type.getDescription(), component.getName(), volume);
    }
}
