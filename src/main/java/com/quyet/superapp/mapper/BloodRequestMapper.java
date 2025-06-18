package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodRequestDTO;
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.User;

public class BloodRequestMapper {

    public static BloodRequestDTO toDTO(BloodRequest req) {
        return new BloodRequestDTO(
                req.getBloodRequestId(),
                req.getRequester().getUserId(),
                req.getBloodType().getBloodTypeId(),
                req.getComponent().getBloodComponentId(),
                req.getQuantityMl(),
                req.getUrgencyLevel(),
                req.getStatus(),
                req.getCreatedAt(),
                req.getConfirmedVolumeMl()
        );
    }

    public static BloodRequest toEntity(BloodRequestDTO dto, User requester, BloodType bloodType, BloodComponent component) {
        BloodRequest entity = new BloodRequest();
        entity.setRequester(requester);
        entity.setBloodType(bloodType);
        entity.setComponent(component);
        entity.setQuantityMl(dto.getQuantityMl());
        entity.setUrgencyLevel(dto.getUrgencyLevel());
        entity.setStatus(dto.getStatus());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setConfirmedVolumeMl(entity.getConfirmedVolumeMl());
        return entity;
    }
}
