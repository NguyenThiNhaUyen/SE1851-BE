package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodInventoryDTO;
import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.mapper.BloodInventoryMapper;
import com.quyet.superapp.repository.BloodInventoryRepository;
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
                    blood.setTotalQuantityMl(updated.getTotalQuantityMl());
                    blood.setLastUpdated(LocalDateTime.now());
                    return bloodRepo.save(blood);
                }).orElse(null);
    }

    public void deleteInventory(Long id) {
        bloodRepo.deleteById(id);
    }

    public List<BloodInventory> searchBloodByType(String bloodType) {
        return bloodRepo.findByBloodType_Description(bloodType);
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


}
