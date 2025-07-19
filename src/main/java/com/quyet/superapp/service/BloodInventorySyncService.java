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
    private final BloodInventoryRepository inventoryRepository;

    @Transactional
    public void syncInventory(BloodUnit unit) {
        if (unit.getQuantityMl() == null || unit.getQuantityMl() < 10) {
            log.warn("❗ Đơn vị máu có thể tích nhỏ (<10ml), không cập nhật kho: {}", unit.getUnitCode());
            return;
        }

        BloodType bloodType = unit.getBloodType();
        BloodComponent component = unit.getComponent();

        Optional<BloodInventory> optional = inventoryRepository.findByBloodTypeAndComponent(bloodType, component);

        BloodInventory inventory = optional.orElseGet(() -> {
            BloodInventory newEntry = new BloodInventory();
            newEntry.setBloodType(bloodType);
            newEntry.setComponent(component);
            newEntry.setTotalQuantityMl(0);
            newEntry.setCreatedAt(LocalDateTime.now());
            return newEntry;
        });

        inventory.setTotalQuantityMl(inventory.getTotalQuantityMl() + unit.getQuantityMl());
        inventory.setLastUpdated(LocalDateTime.now());

        inventoryRepository.save(inventory);
        log.info("✅ Đồng bộ kho máu: {} + {}ml", component.getName(), unit.getQuantityMl());
    }
}
