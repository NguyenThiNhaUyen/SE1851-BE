package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.repository.BloodInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final BloodInventoryRepository bloodInventoryRepository;

    /**
     * ✅ Kiểm tra kho máu có đủ hay không
     * @param bloodTypeId nhóm máu (A, B, AB, O...)
     * @param componentId loại thành phần máu (Hồng cầu, Tiểu cầu...)
     * @param requiredQuantity đơn vị ml yêu cầu
     * @return true nếu đủ, false nếu không đủ hoặc không tìm thấy
     */
    public boolean hasEnough(Long bloodTypeId, Long componentId, Integer requiredQuantity) {
        if (bloodTypeId == null || componentId == null || requiredQuantity == null || requiredQuantity <= 0) {
            throw new IllegalArgumentException("Thông tin yêu cầu không hợp lệ.");
        }

        return bloodInventoryRepository.findByTypeAndComponent(bloodTypeId, componentId)
                .map(inventory -> inventory.getTotalQuantityMl() >= requiredQuantity)
                .orElse(false);
    }
}
