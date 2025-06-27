package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodRequestDTO;
import com.quyet.superapp.dto.CreateBloodRequestDTO;
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
                req.getConfirmedVolumeMl(),
                req.getReason(),
                req.getIsUnmatched(),
                req.getTriageLevel(),
                req.getCodeRedId(),
                req.getEmergencyNote(),
                req.getApprovedBy(),
                req.getApprovedAt()
        );
    }

    public static BloodRequest toEntity(CreateBloodRequestDTO dto, User user, BloodType bloodType, BloodComponent component) {
        BloodRequest entity = new BloodRequest();
        entity.setRequester(user);
        entity.setBloodType(bloodType);
        entity.setComponent(component);
        entity.setQuantityMl(dto.getQuantityMl());
        entity.setUrgencyLevel(dto.getUrgencyLevel());
        entity.setReason(dto.getReason());
        entity.setIsUnmatched(dto.getIsUnmatched());
        entity.setTriageLevel(dto.getTriageLevel());
        entity.setCodeRedId(dto.getCodeRedId());
        return entity;
    }

}
