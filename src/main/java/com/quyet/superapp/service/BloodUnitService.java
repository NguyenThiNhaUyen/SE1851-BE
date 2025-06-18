package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodUnit;
import com.quyet.superapp.repository.BloodUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BloodUnitService {
    private final BloodUnitRepository repository;

    public List<BloodUnit> getAll() {
        return repository.findAll();
    }

    public Optional<BloodUnit> getById(Long id) {
        return repository.findById(id);
    }

    public BloodUnit save(BloodUnit unit) {
        return repository.save(unit);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
