package com.quyet.superapp.service;


import com.quyet.superapp.entity.*;

import com.quyet.superapp.dto.BloodInventoryDTO;
import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.enums.BloodUnitStatus;
import com.quyet.superapp.mapper.BloodInventoryMapper;

import com.quyet.superapp.mapper.BloodUnitMapper;
import com.quyet.superapp.repository.BloodInventoryRepository;
import com.quyet.superapp.repository.BloodUnitRepository;
import com.quyet.superapp.repository.LabTestResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodService {
    private final BloodInventoryRepository bloodRepo;
    private final BloodUnitRepository bloodUnitRepo;
    private final BloodInventoryRepository bloodInventoryRepo;
    private final LabTestResultRepository labTestResultRepo;

    public List<BloodInventory> getInventory() {
        return bloodRepo.findAll();
    }

    public Optional<BloodInventory> getInventoryById(Long id) {
        return bloodRepo.findById(id);
    }

    public BloodInventory addBlood(BloodInventory inventory) {
        inventory.setLastUpdated(LocalDateTime.now());
        return bloodRepo.save(inventory);
    }

    public BloodInventory updateBlood(Long id, BloodInventory updated) {
        return bloodRepo.findById(id)
                .map(blood -> {
                    blood.setBloodType(updated.getBloodType());
                    blood.setComponent(updated.getComponent());
                    blood.setTotalQuantityMl(updated.getTotalQuantityMl());
                    blood.setLastUpdated(LocalDateTime.now());
                    return bloodRepo.save(blood);
                }).orElse(null);
    }

    public void deleteInventory(Long id) {
        bloodRepo.deleteById(id);
    }

    public Optional<BloodInventory> searchBloodByTypeAndComponent(BloodType bloodType, BloodComponent component) {
        return bloodRepo.findByBloodTypeAndComponent(bloodType, component);
    }


    public List<BloodInventoryDTO> getAllDTOs() {
        return bloodRepo.findAll().stream()
                .map(BloodInventoryMapper::toDTO)
                .collect(Collectors.toList());
    }


    public List<BloodInventoryDTO> getAllInventoryDTO() {
        return bloodRepo.findAll().stream()
                .map(BloodInventoryMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public void storeBloodUnit(Long bloodUnitId) {
        BloodUnit unit = bloodUnitRepo.findById(bloodUnitId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn vị máu"));

        // 1. Kiểm tra kết quả xét nghiệm
        LabTestResult test = labTestResultRepo.findByBloodUnit_BloodUnitId(bloodUnitId)
                .orElseThrow(() -> new IllegalStateException("Chưa có kết quả xét nghiệm"));
        if (!test.isPassed()) {
            throw new IllegalStateException("Đơn vị máu không đạt yêu cầu xét nghiệm");
        }

        // 2. Cập nhật trạng thái và thời gian lưu
        unit.setStatus(BloodUnitStatus.STORED);
        unit.setStoredAt(LocalDateTime.now());
        bloodUnitRepo.save(unit);

        // 3. Cập nhật kho máu
        BloodType type = unit.getBloodType();
        BloodComponent component = unit.getComponent();
        BloodInventory inventory = bloodInventoryRepo.findByBloodTypeAndComponent(type, component)
                .orElseGet(() -> {
                    BloodInventory newInv = new BloodInventory();
                    newInv.setBloodType(type);
                    newInv.setComponent(component);
                    newInv.setTotalQuantityMl(0);
                    return newInv;
                });

        int newTotal = inventory.getTotalQuantityMl() + unit.getQuantityMl();
        inventory.setTotalQuantityMl(newTotal);
        inventory.setLastUpdated(LocalDateTime.now());

        bloodInventoryRepo.save(inventory);
    }

}

