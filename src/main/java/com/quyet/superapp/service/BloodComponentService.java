package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodComponentDTO;
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.mapper.BloodComponentMapper;
import com.quyet.superapp.repository.BloodComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodComponentService {

    private final BloodComponentRepository repository;

    /**
     * Lấy tất cả thành phần máu
     */
    public List<BloodComponentDTO> getAll() {
        return repository.findAll().stream()
                .map(BloodComponentMapper::toDTO)
                .toList();
    }

    /**
     * Lấy thành phần máu theo ID
     */
    public BloodComponentDTO getById(Long id) {
        BloodComponent component = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thành phần máu với ID: " + id));
        return BloodComponentMapper.toDTO(component);
    }

    /**
     * Tạo thành phần máu mới
     */
    public BloodComponentDTO create(BloodComponentDTO dto) {
        BloodComponent entity = BloodComponentMapper.toEntity(dto);
        return BloodComponentMapper.toDTO(repository.save(entity));
    }

    /**
     * Cập nhật thông tin thành phần máu
     */
    public BloodComponentDTO update(Long id, BloodComponentDTO dto) {
        BloodComponent existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thành phần máu với ID: " + id));

        existing.setName(dto.getName());
        existing.setCode(dto.getCode());
        existing.setStorageTemp(dto.getStorageTemp());
        existing.setStorageDays(dto.getStorageDays());
        existing.setUsage(dto.getUsage());
        existing.setIsApheresisCompatible(dto.getIsApheresisCompatible());

        return BloodComponentMapper.toDTO(repository.save(existing));
    }

    /**
     * Xoá thành phần máu
     */
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Không tồn tại thành phần máu với ID: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Tìm thành phần máu theo mã code (VD: PRC)
     */
    public BloodComponentDTO getByCode(String code) {
        BloodComponent found = repository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã thành phần máu: " + code));
        return BloodComponentMapper.toDTO(found);
    }
}
