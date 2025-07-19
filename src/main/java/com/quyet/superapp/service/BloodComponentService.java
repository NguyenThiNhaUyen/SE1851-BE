<<<<<<< HEAD
//package com.quyet.superapp.service;
//
//import com.quyet.superapp.dto.BloodComponentDTO;
//import com.quyet.superapp.entity.BloodComponent;
//import com.quyet.superapp.mapper.BloodComponentMapper;
//import com.quyet.superapp.repository.BloodComponentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class BloodComponentService {
//
//    private final BloodComponentRepository bloodComponentRepository;
//    private final BloodComponentMapper mapper;
//
//    public List<BloodComponentDTO> getAvailableComponents() {
//        return bloodComponentRepository.findByIsApheresisCompatibleTrue()
//                .stream()
//                .map(mapper::toDTO)
//                .toList();
//    }
//
//
//    public List<BloodComponentDTO> getAll() {
//        return bloodComponentRepository.findAll()
//                .stream()
//                .map(mapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    public BloodComponentDTO create(BloodComponentDTO dto) {
//        BloodComponent entity = mapper.toEntity(dto);
//        return mapper.toDTO(bloodComponentRepository.save(entity));
//    }
//
//    public BloodComponentDTO update(Long id, BloodComponentDTO dto) {
//        BloodComponent existing = bloodComponentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành phần máu"));
//
//        existing.setName(dto.getName());
//        existing.setCode(dto.getCode());
//        existing.setStorageTemp(dto.getStorageTemp());
//        existing.setStorageDays(dto.getStorageDays());
//        existing.setUsage(dto.getUsage());
//        existing.setIsApheresisCompatible(dto.getIsApheresisCompatible());
//
//        return mapper.toDTO(bloodComponentRepository.save(existing));
//    }
//
//    public void delete(Long id) {
//        if (!bloodComponentRepository.existsById(id)) {
//            throw new RuntimeException("Không tìm thấy thành phần máu để xoá");
//        }
//        bloodComponentRepository.deleteById(id);
//    }
//}

package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodComponentDTO;
import com.quyet.superapp.dto.BloodComponentFullDTO;
import com.quyet.superapp.dto.BloodComponentUpdateDTO;
import com.quyet.superapp.entity.BloodComponent;
=======
package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodComponentDTO;
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.exception.ResourceNotFoundException;
>>>>>>> origin/main
import com.quyet.superapp.mapper.BloodComponentMapper;
import com.quyet.superapp.repository.BloodComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodComponentService {

<<<<<<< HEAD
    private final BloodComponentRepository bloodComponentRepository;
    private final BloodComponentMapper mapper;

    public List<BloodComponentFullDTO> getAllFull() {
        return bloodComponentRepository.findAllByOrderByBloodComponentIdAsc()
                .stream()
                .map(mapper::toFullDTO)
                .collect(Collectors.toList());
    }


    /**
     * Partial update: chỉ gán những trường không null
     */
    public BloodComponentDTO partialUpdate(Long id, BloodComponentUpdateDTO dto) {
        BloodComponent e = bloodComponentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành phần máu"));

        if (dto.getName() != null) e.setName(dto.getName());
        if (dto.getCode() != null) e.setCode(dto.getCode());
        if (dto.getStorageTemp() != null) e.setStorageTemp(dto.getStorageTemp());
        if (dto.getStorageDays() != null) e.setStorageDays(dto.getStorageDays());
        if (dto.getUsage() != null) e.setUsage(dto.getUsage());
        if (dto.getIsApheresisCompatible() != null)
            e.setIsApheresisCompatible(dto.getIsApheresisCompatible());
        if (dto.getType() != null) e.setType(dto.getType());
        if (dto.getIsActive() != null) e.setIsActive(dto.getIsActive());

        BloodComponent saved = bloodComponentRepository.save(e);
        return mapper.toDTO(saved);
    }

    /**
     * Lấy danh sách các thành phần máu tương thích apheresis (active)
     */
    public List<BloodComponentDTO> getAvailableComponents() {
        return bloodComponentRepository.findByIsApheresisCompatibleTrueAndIsActiveTrueOrderByBloodComponentIdAsc()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy tất cả thành phần máu
     */
    public List<BloodComponentDTO> getAll() {
        return bloodComponentRepository.findAllByOrderByBloodComponentIdAsc()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Tạo mới thành phần máu
     */
    public BloodComponentDTO create(BloodComponentDTO dto) {
        BloodComponent entity = mapper.toEntity(dto);
        entity.setIsActive(true);
        BloodComponent saved = bloodComponentRepository.save(entity);
        return mapper.toDTO(saved);
    }

    /**
     * Cập nhật tất cả trường (full update)
     */
    public BloodComponentDTO update(Long id, BloodComponentDTO dto) {
        BloodComponent existing = bloodComponentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành phần máu"));
=======
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
>>>>>>> origin/main

        existing.setName(dto.getName());
        existing.setCode(dto.getCode());
        existing.setStorageTemp(dto.getStorageTemp());
        existing.setStorageDays(dto.getStorageDays());
        existing.setUsage(dto.getUsage());
        existing.setIsApheresisCompatible(dto.getIsApheresisCompatible());
<<<<<<< HEAD
        existing.setType(dto.getType());
        existing.setIsActive(dto.getIsActive());

        BloodComponent saved = bloodComponentRepository.save(existing);
        return mapper.toDTO(saved);
    }

    /**
     * Vô hiệu hóa (deactivate) thành phần máu (isActive = false)
     */
    public void deactivate(Long id) {
        BloodComponent existing = bloodComponentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành phần máu"));
        existing.setIsActive(false);
        bloodComponentRepository.save(existing);
    }

    /**
     * Khôi phục (reactivate) thành phần máu (isActive = true)
     */
    public void reactivate(Long id) {
        BloodComponent existing = bloodComponentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành phần máu"));
        existing.setIsActive(true);
        bloodComponentRepository.save(existing);
    }
}

=======

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
>>>>>>> origin/main
