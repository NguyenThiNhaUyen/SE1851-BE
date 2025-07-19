package com.quyet.superapp.controller;

import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.service.BloodService;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
=======
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
>>>>>>> origin/main
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blood")
@RequiredArgsConstructor
<<<<<<< HEAD
=======
@Validated
>>>>>>> origin/main
public class BloodController {

    private final BloodService bloodService;

    // --- BLOOD INVENTORY ---
<<<<<<< HEAD

=======
>>>>>>> origin/main
    @GetMapping("/inventory")
    public List<BloodInventory> getAllInventory() {
        return bloodService.getInventory();
    }

<<<<<<< HEAD
    @GetMapping("/inventory/{id}")
    public ResponseEntity<BloodInventory> getInventoryById(@PathVariable Long id) {
=======
    @GetMapping("/inventory/by-id")
    public ResponseEntity<BloodInventory> getInventoryById(@RequestParam Long id) {
>>>>>>> origin/main
        return bloodService.getInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/inventory/create")
<<<<<<< HEAD
    public BloodInventory createInventory(@RequestBody BloodInventory inventory) {
        return bloodService.addBlood(inventory);
    }

    @PutMapping("/inventory/{id}")
    public ResponseEntity<BloodInventory> updateInventory(@PathVariable Long id, @RequestBody BloodInventory updated) {
=======
    public BloodInventory createInventory( @RequestBody BloodInventory inventory) {
        return bloodService.addBlood(inventory);
    }

    @PutMapping("/inventory/update")
    public ResponseEntity<BloodInventory> updateInventory(@RequestParam Long id, @RequestBody BloodInventory updated) {
>>>>>>> origin/main
        BloodInventory result = bloodService.updateBlood(id, updated);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

<<<<<<< HEAD
    @DeleteMapping("/inventory/{id}")
    public void deleteInventory(@PathVariable Long id) {
=======
    @DeleteMapping("/inventory/delete")
    public void deleteInventory(@RequestParam Long id) {
>>>>>>> origin/main
        bloodService.deleteInventory(id);
    }
}
