package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodComponentDTO;
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.mapper.BloodComponentMapper;
import com.quyet.superapp.repository.BloodComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodComponentService {

    private final BloodComponentRepository componentRepo;

    // 🔍 Lấy toàn bộ
    public List<BloodComponentDTO> getAll() {
        return componentRepo.findAll().stream()
                .map(BloodComponentMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 🔍 Lấy theo ID
    public BloodComponentDTO getById(Long id) {
        BloodComponent component = componentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thành phần máu với ID: " + id));
        return BloodComponentMapper.toDTO(component);
    }

    // ✅ Tạo mới
    public BloodComponentDTO create(BloodComponentDTO dto) {
        BloodComponent entity = BloodComponentMapper.toEntity(dto);
        BloodComponent saved = componentRepo.save(entity);
        return BloodComponentMapper.toDTO(saved);
    }

    // ✅ Cập nhật
    public BloodComponentDTO update(Long id, BloodComponentDTO dto) {
        BloodComponent existing = componentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thành phần máu"));

        existing.setName(dto.getName());
        existing.setCode(dto.getCode());
        existing.setStorageTemp(dto.getStorageTemp());
        existing.setStorageDays(dto.getStorageDays());
        existing.setUsage(dto.getUsage());
        existing.setIsApheresisCompatible(dto.getIsApheresisCompatible());

        BloodComponent updated = componentRepo.save(existing);
        return BloodComponentMapper.toDTO(updated);
    }

    // ❌ Xoá
    public void delete(Long id) {
        if (!componentRepo.existsById(id)) {
            throw new ResourceNotFoundException("Không tồn tại thành phần máu");
        }
        componentRepo.deleteById(id);
    }

    // 🔍 Tìm theo mã code (ví dụ: PRC)
    public BloodComponentDTO getByCode(String code) {
        BloodComponent found = componentRepo.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã thành phần máu: " + code));
        return BloodComponentMapper.toDTO(found);
    }
}
