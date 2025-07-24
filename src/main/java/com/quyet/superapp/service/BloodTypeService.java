package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodTypeDTO;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.mapper.BloodTypeMapper;
import com.quyet.superapp.repository.BloodTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodTypeService {

    private final BloodTypeRepository bloodTypeRepository;

    public List<BloodTypeDTO> getAll() {
        return bloodTypeRepository.findAll().stream()
                .map(BloodTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BloodTypeDTO create(BloodTypeDTO dto) {
        BloodType entity = BloodTypeMapper.toEntity(dto);
        BloodType saved = bloodTypeRepository.save(entity);
        return BloodTypeMapper.toDTO(saved);
    }

    public void delete(Long id) {
        if (!bloodTypeRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy nhóm máu");
        }
        bloodTypeRepository.deleteById(id);
    }


}
