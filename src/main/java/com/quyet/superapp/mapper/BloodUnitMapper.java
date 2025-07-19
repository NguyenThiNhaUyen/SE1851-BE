<<<<<<< HEAD
    package com.quyet.superapp.mapper;

    import com.quyet.superapp.dto.BloodUnitDTO;
    import com.quyet.superapp.entity.*;
    import com.quyet.superapp.enums.BloodUnitStatus;

    public class BloodUnitMapper {
        public static BloodUnitDTO toDTO(BloodUnit unit) {
            return new BloodUnitDTO(
                    unit.getBloodUnitId(),
                    unit.getBloodType() != null ? unit.getBloodType().getBloodTypeId() : null,
                    unit.getComponent() != null ? unit.getComponent().getBloodComponentId() : null,
                    unit.getBloodBag() != null ? unit.getBloodBag().getBloodBagId() : null,
                    unit.getQuantityMl(),
                    unit.getExpirationDate(),
                    unit.getStatus() != null ? unit.getStatus().name() : null,
                    unit.getStoredAt(),
                    unit.getCreatedAt(),
                    unit.getUpdatedAt(),
                    unit.getUnitCode(),
                    unit.getBloodType().getDescription(),
                    unit.getComponent().getName()

            );
        }

        /**
         * fromDTO version đầy đủ — yêu cầu truyền entity liên kết đã được tìm trước
         */
        public static BloodUnit fromDTO(
                BloodUnitDTO dto,
                BloodType bloodType,
                BloodComponent component,
                BloodBag bloodBag,
                SeparationOrder separationOrder
        ) {
            BloodUnit unit = new BloodUnit();
            unit.setBloodUnitId(dto.getBloodUnitId());
            unit.setBloodType(bloodType);
            unit.setComponent(component);
            unit.setBloodBag(bloodBag);
            unit.setSeparationOrder(separationOrder);
            unit.setQuantityMl(dto.getQuantityMl());
            unit.setExpirationDate(dto.getExpirationDate());

            if (dto.getStatus() != null) {
                try {
                    unit.setStatus(BloodUnitStatus.valueOf(dto.getStatus().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    unit.setStatus(null); // hoặc default status nếu muốn
                }
            }

            unit.setStoredAt(dto.getStoredAt());
            unit.setCreatedAt(dto.getCreatedAt());
            unit.setUpdatedAt(dto.getUpdatedAt());
            return unit;
        }

        /**
         * fromDTO đơn giản — chỉ khi không cần gán quan hệ
         */
        public static BloodUnit fromDTO(BloodUnitDTO dto) {
            return fromDTO(dto, null, null, null, null);
        }
    }
=======
package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodUnitDTO;
import com.quyet.superapp.dto.BloodUnitCreateDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.BloodUnitStatus;

public class BloodUnitMapper {

    // 🩸 Convert Entity → DTO (để hiển thị)
    public static BloodUnitDTO toDTO(BloodUnit unit) {
        if (unit == null) return null;

        BloodUnitDTO dto = new BloodUnitDTO();

        dto.setBloodUnitId(unit.getBloodUnitId());
        dto.setUnitCode(unit.getUnitCode());

        dto.setQuantityMl(unit.getQuantityMl());
        dto.setExpirationDate(unit.getExpirationDate());
        dto.setStatus(unit.getStatus() != null ? unit.getStatus().name() : null);

        dto.setStoredAt(unit.getStoredAt());
        dto.setCreatedAt(unit.getCreatedAt());
        dto.setUpdatedAt(unit.getUpdatedAt());

        if (unit.getBloodType() != null) {
            dto.setBloodTypeId(unit.getBloodType().getBloodTypeId());
            dto.setBloodTypeName(unit.getBloodType().getDescription());
        }

        if (unit.getComponent() != null) {
            dto.setComponentId(unit.getComponent().getBloodComponentId());
            dto.setComponentName(unit.getComponent().getName());
        }

        if (unit.getBloodBag() != null) {
            dto.setBloodBagId(unit.getBloodBag().getBloodBagId());
            dto.setBagCode(unit.getBloodBag().getBagCode());
        }

        return dto;
    }

    // 🧪 Convert CreateDTO → Entity (dùng khi tạo mới)
    public static BloodUnit fromCreateDTO(
            BloodUnitCreateDTO dto,
            BloodType bloodType,
            BloodComponent component,
            BloodBag bloodBag,
            SeparationOrder order
    ) {
        BloodUnit unit = new BloodUnit();
        unit.setBloodType(bloodType);
        unit.setComponent(component);
        unit.setBloodBag(bloodBag);
        unit.setSeparationOrder(order);
        unit.setQuantityMl(dto.getQuantityMl());
        unit.setExpirationDate(dto.getExpirationDate());

        try {
            unit.setStatus(BloodUnitStatus.valueOf(dto.getStatus().toUpperCase()));
        } catch (IllegalArgumentException e) {
            unit.setStatus(BloodUnitStatus.STORED); // hoặc set mặc định
        }

        return unit;
    }

    // 🧪 Phiên bản đầy đủ của fromDTO để update
    public static BloodUnit fromDTO(
            BloodUnitDTO dto,
            BloodType bloodType,
            BloodComponent component,
            BloodBag bloodBag,
            SeparationOrder separationOrder
    ) {
        BloodUnit unit = new BloodUnit();
        unit.setBloodUnitId(dto.getBloodUnitId());
        unit.setBloodType(bloodType);
        unit.setComponent(component);
        unit.setBloodBag(bloodBag);
        unit.setSeparationOrder(separationOrder);
        unit.setQuantityMl(dto.getQuantityMl());
        unit.setExpirationDate(dto.getExpirationDate());

        try {
            unit.setStatus(BloodUnitStatus.valueOf(dto.getStatus().toUpperCase()));
        } catch (IllegalArgumentException e) {
            unit.setStatus(null);
        }

        unit.setStoredAt(dto.getStoredAt());
        unit.setCreatedAt(dto.getCreatedAt());
        unit.setUpdatedAt(dto.getUpdatedAt());

        return unit;
    }

    // Phiên bản đơn giản
    public static BloodUnit fromDTO(BloodUnitDTO dto) {
        return fromDTO(dto, null, null, null, null);
    }
}
>>>>>>> origin/main
