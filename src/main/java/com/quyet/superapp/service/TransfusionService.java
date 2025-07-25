package com.quyet.superapp.service;

import com.quyet.superapp.entity.Transfusion;
import com.quyet.superapp.repository.TransfusionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import java.util.List;



@Service
@RequiredArgsConstructor
public class TransfusionService {
    private final TransfusionRepository repo;

    public List<Transfusion> findAll() {
        return repo.findAll();
    }


    public Transfusion findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi truyền máu với ID: " + id));
    }
}

