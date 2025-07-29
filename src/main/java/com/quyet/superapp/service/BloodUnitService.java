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

    private final BloodUnitRepository bloodUnitRepository;
    private final BloodTypeRepository bloodTypeRepository;
    private final BloodComponentRepository componentRepository;
    private final BloodBagRepository bloodBagRepository;

    /**
     * Lấy toàn bộ đơn vị máu trong hệ thống.
     */
    public List<BloodUnit> getAllUnits() {
        return bloodUnitRepository.findAll();
    }

    /**
     * Lấy đơn vị máu theo ID.
     * @throws ResourceNotFoundException nếu không tìm thấy đơn vị máu.
     */
    public BloodUnit getById(Long id) {
        return bloodUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn vị máu với ID: " + id));
    }

    /**
     * Lấy đơn vị máu theo mã code.
     * @throws ResourceNotFoundException nếu không tìm thấy đơn vị máu.
     */
    public BloodUnit getByUnitCode(String code) {
        return bloodUnitRepository.findByUnitCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn vị máu với mã: " + code));
    }

    /**
     * Tạo hoặc cập nhật đơn vị máu.
     * Tự động ánh xạ các entity liên quan từ ID: nhóm máu, thành phần, túi máu.
     */
    public BloodUnit saveUnit(BloodUnit unit, Long bloodTypeId, Long componentId, Long bloodBagId) {
        BloodType bloodType = bloodTypeRepository.findById(bloodTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhóm máu với ID: " + bloodTypeId));

        BloodComponent component = componentRepository.findById(componentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thành phần máu với ID: " + componentId));

        BloodBag bloodBag = bloodBagRepository.findById(bloodBagId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy túi máu với ID: " + bloodBagId));

        unit.setBloodType(bloodType);
        unit.setComponent(component);
        unit.setBloodBag(bloodBag);

        return bloodUnitRepository.save(unit);
    }

    /**
     * Xoá đơn vị máu theo ID.
     * @throws ResourceNotFoundException nếu không tồn tại đơn vị máu.
     */
    public void deleteById(Long id) {
        if (!bloodUnitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tồn tại đơn vị máu với ID: " + id);
        }
        bloodUnitRepository.deleteById(id);
    }

    /**
     * Lấy danh sách đơn vị máu theo trạng thái.
     */
    public List<BloodUnit> findByStatus(BloodUnitStatus status) {
        return bloodUnitRepository.findByStatus(status);
    }

    /**
     * Lấy danh sách đơn vị máu có ngày hết hạn trước ngày chỉ định.
     */
    public List<BloodUnit> findExpiringBefore(LocalDate date) {
        return bloodUnitRepository.findByExpirationDateBefore(date);
    }
}
