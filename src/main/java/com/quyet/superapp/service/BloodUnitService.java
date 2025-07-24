package com.quyet.superapp.service;

import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.BloodUnitStatus;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodUnitService {

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
}
