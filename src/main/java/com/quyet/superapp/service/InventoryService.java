package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.repository.BloodInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> origin/main
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final BloodInventoryRepository inventoryRepo;

    /**
     * Kiểm tra xem kho máu có đủ không
     */
    public boolean hasEnough(Long bloodTypeId, Long componentId, Integer requiredQuantity) {
<<<<<<< HEAD
        List<BloodInventory> inventories = inventoryRepo.findByTypeAndComponent(bloodTypeId, componentId);

        if (inventories.isEmpty()) {
            return false;
        }

        BloodInventory inventory = inventories.get(0);
=======
        BloodInventory inventory = inventoryRepo.findByTypeAndComponent(bloodTypeId, componentId)
                .orElse(null);

        if (inventory == null) return false;

>>>>>>> origin/main
        return inventory.getTotalQuantityMl() >= requiredQuantity;
    }
}
