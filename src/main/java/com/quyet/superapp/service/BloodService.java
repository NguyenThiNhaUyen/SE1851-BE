package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.repository.BloodInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
}

