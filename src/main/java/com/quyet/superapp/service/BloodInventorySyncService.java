package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.BloodUnit;
import com.quyet.superapp.repository.BloodInventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BloodInventorySyncService {

    private static final int MIN_VALID_QUANTITY_ML = 10;

    private final BloodInventoryRepository inventoryRepository;

    /**
     * Đồng bộ số lượng máu từ đơn vị máu vào kho máu tổng.
     * Nếu chưa tồn tại bản ghi phù hợp sẽ tạo mới.
     *
     * @param unit đơn vị máu đã sinh
     */
    @Transactional
    public void syncInventory(BloodUnit unit) {
        if (isInvalidUnit(unit)) {
            log.warn("❗ Đơn vị máu không hợp lệ (null hoặc <{}ml), bỏ qua: {}",
                    MIN_VALID_QUANTITY_ML, unit.getUnitCode());
            return;
        }

        BloodType bloodType = unit.getBloodType();
        BloodComponent component = unit.getComponent();

        BloodInventory inventory = inventoryRepository
                .findByBloodTypeAndComponent(bloodType, component)
                .orElseGet(() -> createNewInventory(bloodType, component));

        int updatedQuantity = inventory.getTotalQuantityMl() + unit.getQuantityMl();
        inventory.setTotalQuantityMl(updatedQuantity);
        inventory.setLastUpdated(LocalDateTime.now());

        inventoryRepository.save(inventory);

        log.info("✅ Cập nhật kho máu: [{} - {}] +{}ml (Tổng mới: {}ml)",
                bloodType.getDisplayName(), component.getName(),
                unit.getQuantityMl(), updatedQuantity);
    }

    private boolean isInvalidUnit(BloodUnit unit) {
        return unit.getQuantityMl() == null || unit.getQuantityMl() < MIN_VALID_QUANTITY_ML;
    }

    private BloodInventory createNewInventory(BloodType bloodType, BloodComponent component) {
        BloodInventory inventory = new BloodInventory();
        inventory.setBloodType(bloodType);
        inventory.setComponent(component);
        inventory.setTotalQuantityMl(0);
        inventory.setCreatedAt(LocalDateTime.now());
        return inventory;
    }
}
