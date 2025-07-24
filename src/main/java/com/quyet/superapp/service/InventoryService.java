package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.repository.BloodInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final BloodInventoryRepository inventoryRepo;

    /**
     * Kiểm tra xem kho máu có đủ không
     */
    public boolean hasEnough(Long bloodTypeId, Long componentId, Integer requiredQuantity) {
        BloodInventory inventory = inventoryRepo.findByTypeAndComponent(bloodTypeId, componentId)
                .orElse(null);

        if (inventory == null) return false;

        return inventory.getTotalQuantityMl() >= requiredQuantity;
    }
}
