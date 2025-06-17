package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.entity.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {
        Optional<BloodInventory> findByBloodTypeAndComponent(BloodType bloodType, BloodComponent component);

}