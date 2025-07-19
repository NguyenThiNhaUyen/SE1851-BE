package com.quyet.superapp.service;

<<<<<<< HEAD
import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.BloodUnit;
import com.quyet.superapp.enums.BloodUnitStatus;
import com.quyet.superapp.repository.BloodBagRepository;
import com.quyet.superapp.repository.BloodComponentRepository;
import com.quyet.superapp.repository.BloodTypeRepository;
import com.quyet.superapp.repository.BloodUnitRepository;
=======
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.BloodUnitStatus;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.repository.*;
>>>>>>> origin/main
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> origin/main

@Service
@RequiredArgsConstructor
public class BloodUnitService {
<<<<<<< HEAD
    private final BloodUnitRepository repository;
    private final BloodBagRepository bloodBagRepository;
    private final BloodTypeRepository bloodTypeRepository;
    private final BloodComponentRepository bloodComponentRepository;

    // ✅ Lấy tất cả đơn vị máu
    public List<BloodUnit> getAll() {
        return repository.findAll();
    }

    // ✅ Lấy đơn vị máu theo ID
    public BloodUnit getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blood unit not found with id: " + id));
    }

    // ✅ Lưu mới hoặc cập nhật đơn vị máu
    public BloodUnit save(BloodUnit unit, Long bloodTypeId, Long componentId, Long bloodBagId) {
        BloodType bloodType = bloodTypeRepository.findById(bloodTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Blood type not found"));
        BloodComponent component = bloodComponentRepository.findById(componentId)
                .orElseThrow(() -> new IllegalArgumentException("Component not found"));
        BloodBag bag = bloodBagRepository.findById(bloodBagId)
                .orElseThrow(() -> new IllegalArgumentException("Blood bag not found"));

        unit.setBloodType(bloodType);
        unit.setComponent(component);
        unit.setBloodBag(bag);

        return repository.save(unit);
    }

    // ✅ Xóa đơn vị máu
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Blood unit does not exist with id: " + id);
        }
        repository.deleteById(id);
    }
    // ✅ Tìm đơn vị máu theo trạng thái
    public List<BloodUnit> findByStatus(BloodUnitStatus status) {
        return repository.findByStatus(status);
    }

    // ✅ Tìm đơn vị máu sắp hết hạn
    public List<BloodUnit> findExpiringBefore(LocalDate date) {
        return repository.findByExpirationDateBefore(date);
    }

    // ✅ Tìm theo mã đơn vị
    public BloodUnit findByUnitCode(String code) {
        return repository.findByUnitCode(code)
                .orElseThrow(() -> new IllegalArgumentException("No blood unit found with code: " + code));
    }

=======

    private final BloodUnitRepository bloodUnitRepo;
    private final BloodTypeRepository bloodTypeRepo;
    private final BloodComponentRepository componentRepo;
    private final BloodBagRepository bagRepo;

    // 🔍 Lấy toàn bộ đơn vị máu
    public List<BloodUnit> getAllUnits() {
        return bloodUnitRepo.findAll();
    }

    // 🔍 Lấy theo ID
    public BloodUnit getById(Long id) {
        return bloodUnitRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn vị máu với ID: " + id));
    }

    // 🔍 Lấy theo mã code (để tra cứu)
    public BloodUnit getByUnitCode(String code) {
        return bloodUnitRepo.findByUnitCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn vị máu với mã: " + code));
    }

    // 🗃️ Lưu đơn vị máu – gán các entity liên kết theo ID
    public BloodUnit saveUnit(BloodUnit unit, Long bloodTypeId, Long componentId, Long bloodBagId) {
        BloodType bloodType = bloodTypeRepo.findById(bloodTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhóm máu với ID: " + bloodTypeId));

        BloodComponent component = componentRepo.findById(componentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thành phần máu với ID: " + componentId));

        BloodBag bloodBag = bagRepo.findById(bloodBagId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy túi máu với ID: " + bloodBagId));

        unit.setBloodType(bloodType);
        unit.setComponent(component);
        unit.setBloodBag(bloodBag);

        return bloodUnitRepo.save(unit);
    }

    // ❌ Xóa theo ID
    public void deleteById(Long id) {
        if (!bloodUnitRepo.existsById(id)) {
            throw new ResourceNotFoundException("Không tồn tại đơn vị máu với ID: " + id);
        }
        bloodUnitRepo.deleteById(id);
    }

    // 🔍 Tìm đơn vị máu theo trạng thái
    public List<BloodUnit> findByStatus(BloodUnitStatus status) {
        return bloodUnitRepo.findByStatus(status);
    }

    // 🔍 Tìm các đơn vị máu sắp hết hạn
    public List<BloodUnit> findExpiringBefore(LocalDate date) {
        return bloodUnitRepo.findByExpirationDateBefore(date);
    }
>>>>>>> origin/main
}
