package com.quyet.superapp.controller;

import com.quyet.superapp.dto.AddressRequestDTO;
import com.quyet.superapp.entity.address.Address;
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

public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AddressRequestDTO dto) {
        Address saved = addressService.createAddressFromDTO(dto);
        return ResponseEntity.ok(saved);
    }
}