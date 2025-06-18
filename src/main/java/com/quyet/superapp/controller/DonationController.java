package com.quyet.superapp.controller;

import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @GetMapping
    public List<Donation> getAll() {
        return donationService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donation> getById(@PathVariable Long id) {
        return donationService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Donation create(@RequestBody Donation obj) {
        return donationService.save(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Donation> update(@PathVariable Long id, @RequestBody Donation obj) {
        Optional<Donation> existing = donationService.getById(id);
        return existing.isPresent()
                ? ResponseEntity.ok(donationService.save(obj))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        donationService.deleteById(id);
    }
}
