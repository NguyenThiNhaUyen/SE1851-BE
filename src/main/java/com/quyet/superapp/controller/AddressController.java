package com.quyet.superapp.controller;

import com.quyet.superapp.dto.AddressRequestDTO;
import com.quyet.superapp.entity.address.Address;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.quyet.superapp.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
=======
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quyet.superapp.service.AddressService;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor

>>>>>>> origin/main
public class AddressController {

    private final AddressService addressService;

<<<<<<< HEAD
    @GetMapping("/nearby-streets")
    public List<String> getNearbyStreetNames(@RequestParam Double lat, @RequestParam Double lng) {
        return addressService.suggestAddressStreets(lat, lng);
    }


    @GetMapping("/search")
    public ResponseEntity<List<String>> searchSimilar(@RequestParam String keyword) {
        List<String> suggestions = addressService.searchSimilarAddresses(keyword);
        return ResponseEntity.ok(suggestions);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AddressRequestDTO dto) {
        Address saved = addressService.createOrUpdateAddress(dto);
=======
    @PostMapping
    public ResponseEntity<?> create(@RequestBody AddressRequestDTO dto) {
        Address saved = addressService.createAddressFromDTO(dto);
>>>>>>> origin/main
        return ResponseEntity.ok(saved);
    }
}