package com.quyet.superapp.controller;

import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.service.BloodService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blood")
@RequiredArgsConstructor

public class BloodController {

    private final BloodService bloodService;

    // --- BLOOD INVENTORY ---

    @GetMapping("/inventory")
    public List<BloodInventory> getAllInventory() {
        return bloodService.getInventory();
    }

 ResponseEntity<BloodInventory> getInventoryById(@PathVariable Long id) {
        return bloodService.getInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/inventory/create")

    public BloodInventory createInventory( @RequestBody BloodInventory inventory) {
        return bloodService.addBlood(inventory);
    }

    @PutMapping("/inventory/update")
    public ResponseEntity<BloodInventory> updateInventory(@RequestParam Long id, @RequestBody BloodInventory updated) {

        BloodInventory result = bloodService.updateBlood(id, updated);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/inventory/delete")
    public void deleteInventory(@RequestParam Long id) {

        bloodService.deleteInventory(id);
    }
}
