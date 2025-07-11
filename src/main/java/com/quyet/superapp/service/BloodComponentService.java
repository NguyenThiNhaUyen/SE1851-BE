package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodComponentDTO;
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.repository.BloodComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodComponentService {

    private final BloodComponentRepository bloodComponentRepository;

    public List<BloodComponentDTO> getAll() {
        return bloodComponentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BloodComponentDTO create(BloodComponentDTO dto) {
        BloodComponent entity = new BloodComponent();
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setStorageTemp(dto.getStorageTemp());
        entity.setStorageDays(dto.getStorageDays());
        entity.setUsage(dto.getUsage());
        entity.setIsApheresisCompatible(dto.getIsApheresisCompatible());
        return toDTO(bloodComponentRepository.save(entity));
    }

    private BloodComponentDTO toDTO(BloodComponent entity) {
        return new BloodComponentDTO(
                entity.getBloodComponentId(),
                entity.getName(),
                entity.getCode(),
                entity.getStorageTemp(),
                entity.getStorageDays(),
                entity.getUsage(),
                entity.getIsApheresisCompatible()
        );
    }
}
