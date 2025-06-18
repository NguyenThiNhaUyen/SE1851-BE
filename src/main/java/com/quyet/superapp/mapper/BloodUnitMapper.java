package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodUnitDTO;
import com.quyet.superapp.entity.BloodUnit;

public class BloodUnitMapper {
    public static BloodUnitDTO toDTO(BloodUnit unit) {
        return new BloodUnitDTO(
                unit.getBloodUnitId(),
                unit.getBloodType().getBloodTypeId(),
                unit.getComponent().getBloodComponentId(),
                unit.getDonation().getDonationId(),
                unit.getQuantityMl(),
                unit.getExpirationDate(),
                unit.getStatus(),
                unit.getStoredAt()
        );
    }
}