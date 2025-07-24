package com.quyet.superapp.controller;

import com.quyet.superapp.dto.TransfusionDTO;
import com.quyet.superapp.mapper.TransfusionMapper;
import com.quyet.superapp.service.TransfusionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transfusions")
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
public class TransfusionController {
    private final TransfusionService svc;
    private final TransfusionMapper map;

    @GetMapping
    public List<TransfusionDTO> getAll() {
        return svc.findAll().stream()
                .map(map::toDTO)
                .toList();
    }
}

